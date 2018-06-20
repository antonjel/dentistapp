package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dao.VisitDetails;
import com.cgi.dentistapp.dao.entity.DoctorEntity;
import com.cgi.dentistapp.dto.DoctorDTO;
import com.cgi.dentistapp.dto.VisitDTO;
import com.cgi.dentistapp.service.DoctorService;
import com.cgi.dentistapp.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class DentistAppController extends WebMvcConfigurerAdapter {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private VisitService visitService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
        registry.addViewController("/registrations").setViewName("registrations");
        registry.addViewController("/appointment-details").setViewName("appointment-details");
        registry.addViewController("/update").setViewName("update");
    }

    // Populate the form model with a list of General Practitioners available in the database and return the form
    @GetMapping("/")
    public String showRegisterForm(DoctorDTO doctorDTO, VisitDTO visitDTO, Model model) {
        model.addAttribute("gpList", doctorService.getGPList());
        return "form";
    }

    /* Upon submitting the dentist registration request:
     * Check whether the provided dentist name and visit time are valid entries
     * If the entries are valid, check whether the provided dentist name already exists within the database
     *  If the name does not exist - create new dentist with the provided name
     * Check whether the provided visit time is available for the dentist
     * If the time is already booked - show error, otherwise create a new visit entry
     */
    @RequestMapping(value = "dentistRegistration", method = RequestMethod.POST)
    public String postDentistForm(@Valid DoctorDTO doctorDTO, BindingResult bindingResult, @Valid VisitDTO visitDTO, BindingResult bindingResultTwo, Model model) {
        if (bindingResult.hasErrors() || bindingResultTwo.hasErrors()) {
            return showRegisterForm(doctorDTO, visitDTO, model);
        }
        String dentistName = doctorDTO.getName();
        if(!doctorService.doctorExists(dentistName)){
            doctorService.addDoctor("Dentist", dentistName);
        }
        Long id = doctorService.getDoctorId(dentistName);

        if(!visitService.isTimeAvailable(id, visitDTO.getVisitTime())){
            model.addAttribute("error", "dentist time already booked");
            return showRegisterForm(doctorDTO, visitDTO, model);
        }

        visitService.addVisit(id, visitDTO.getVisitTime());
        return "redirect:/results";
    }

    /* Upon submitting the general practitioner registration request:
     * Check that a visit time has been selected, otherwise show an error message
     * Check if the visit time for the selected doctor is available, otherwise show an error message
     * If all is fine: Complete the registration for the selected doctor and visit time
     */
    @RequestMapping(value = "gpRegistration", method = RequestMethod.POST)
    public String postDoctorForm(DoctorDTO doctorDTO, VisitDTO visitDTO, @RequestParam("selectedGPName") String gpName, Model model, @RequestParam("selectedVisitTime") String selectedVisitTime) {
        if (selectedVisitTime.equals("")) {
            model.addAttribute("error", "empty visit time");
            return showRegisterForm(doctorDTO, visitDTO, model);
        }
        LocalDate localDate = LocalDate.parse(selectedVisitTime);

        if(!visitService.isTimeAvailable(doctorService.getDoctorId(gpName), localDate)){
            model.addAttribute("error", "gp time already booked");
            return showRegisterForm(doctorDTO, visitDTO, model);
        }
        visitService.addVisit(doctorService.getDoctorId(gpName), localDate);
        return "redirect:/results";
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:registrations";
    }

    /* Get all available visit details for all doctors/dentists
     * Go over the list and separate the doctors accordingly in to their groups
     * Return the page with the populated dentist/doctor registration lists
     */
    @RequestMapping(value = "/registrations", method = RequestMethod.GET)
    public ModelAndView finalPage() {
        List<VisitDetails> scheduledVisits = visitService.getFullVisitDetails();
        List<VisitDetails> dentistVisits = new ArrayList<>();
        List<VisitDetails> doctorVisits = new ArrayList<>();
        for (VisitDetails visit: scheduledVisits) {
            if(visit.getDoctorType().equals("Dentist")) dentistVisits.add(visit);
            else doctorVisits.add(visit);
        }
        ModelAndView modelAndView = new ModelAndView("registrations");
        modelAndView.addObject("dentistVisits", dentistVisits);
        modelAndView.addObject("doctorVisits", doctorVisits);
        return modelAndView;
    }

    /* Search for the doctors that fit the search criteria
     * Create a Map where k = name of the doctor, v = number of visits that are booked for the doctor
     * Display the results based on the created map
     */
    @RequestMapping(value = "doctorSearch", method = RequestMethod.GET)
    public String searchForDoctor(DoctorDTO doctorDTO, VisitDTO visitDTO, BindingResult bindingResult, Model model) {
        List<DoctorEntity> doctors = doctorService.searchForDoctor(doctorDTO.getName());
        Map<String, Integer> doctorVisits = new HashMap<>();
        if(doctors.size()>0){
            for (DoctorEntity doc : doctors) {
                doctorVisits.put(doc.getName(), visitService.getVisitsForDoctor(doc.getId()).size());
            }
        }
        model.addAttribute("doctorVisits", doctorVisits);
        return showRegisterForm(doctorDTO, visitDTO, model);
    }

    @RequestMapping(value = "doctorSearch", method = RequestMethod.GET, params = {"getMoreDetails"})
    public String getMoreDetails(@RequestParam("getMoreDetails") String name, Model model) {
        return viewAppointmentDetails(name, model);
    }

    //Get full appointment details for the relevant doctor and prepare the relevant data for display
    @RequestMapping(value = "appointment-details", method = RequestMethod.GET)
    public String viewAppointmentDetails(String name, Model model){
        List<VisitDetails> visitDetailsList = visitService.getFullVisitDetailsForDoctor(doctorService.getDoctorId(name));
        model.addAttribute("doctorName", name);
        model.addAttribute("visitDetailsList", visitDetailsList);
        return "appointment-details";
    }

    // Delete a specific visit entry based on user's selection
    @RequestMapping(value = "appointmentDetails", method = RequestMethod.POST, params = {"deleteVisit"})
    public String deleteVisit(@RequestParam("deleteVisit") Long visitId, Model model) {
        visitService.deleteVisitEntry(visitId);
        return "redirect:/results";
    }

    /* Prepare and display the visit time update section
     * Collect the relevant visit id as well as the visit time that is being updated
     */
    @RequestMapping(value = "appointmentDetails", method = RequestMethod.POST, params = {"updateVisit"})
    public String updateVisit(@RequestParam("updateVisit") Long visitId, Model model, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("visitId", visitId);
        redirectAttributes.addAttribute("currentVisitTime", visitService.getVisit(visitId).getFormattedVisitTime());
        return "redirect:/update";
    }

    /* Upon updating the visit time:
     * Check whether the new visit time is valid, otherwise show the relevant error,
     * while preserving the relevant visit id and time.
     * Check if the time slot for the new visit time is available for the relevant doctor, otherwise show an error,
     * while preserving the relevant visit id, and time.
     * If everything is in order - update the visit time entry
     */
    @RequestMapping(value = "visitTimeUpdate", method = RequestMethod.POST, params = {"newTime"})
    public String visitTimeUpdate(@Valid VisitDTO visitDTO, BindingResult bindingResult, Model model, @RequestParam("visitId") Long visitId, @RequestParam("currentVisitTime") String currentVisitTime) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("visitId", visitId);
            model.addAttribute("currentVisitTime", currentVisitTime);
            return "update";
        }
        if(!visitService.isTimeAvailable(visitService.getVisit(visitId).getDoctorId(), visitDTO.getVisitTime())){
            model.addAttribute("visitId", visitId);
            model.addAttribute("currentVisitTime", currentVisitTime);
            model.addAttribute("error", "update time already booked");
            return "update";
        }
        visitService.updateVisitEntry(visitId, visitDTO.getVisitTime());
        return "redirect:/results";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String showUpdateScreen(@Valid VisitDTO visitDTO, BindingResult bindingResult, @RequestParam("visitId") Long visitId, @RequestParam("currentVisitTime") String currentVisitTime,  Model model){
        model.addAttribute("visitId", visitId);
        model.addAttribute("currentVisitTime", currentVisitTime);
        return "update";
    }
}
