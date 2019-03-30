package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Course;
import ouhk.comps380f.model.User;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("classroom")
public class ClassroomController {
    private volatile long COURSE_ID_SEQUENCE = 1;
    private Map<Long, Course> courseDatabase = new Hashtable<>();
    
    @RequestMapping("/")
    public String index() {
        return "redirect:/classroom/listCourse";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @RequestMapping(value = {"", "listCourse"}, method = RequestMethod.GET)
    public String listCourse(ModelMap model) {
        model.addAttribute("courseDatabase", courseDatabase);
        return "listCourse";
    }
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("addCourse", "courseForm", new CreateCourseForm());
    }
    
    @RequestMapping(value = "view/{courseId}", method = RequestMethod.GET)
    public String view(@PathVariable("courseId") long courseId, ModelMap model) {
        Course course = this.courseDatabase.get(courseId);
        if(course == null) {
            return "redirect:/classroom/listCourse";
        }
        model.addAttribute("courseId", Long.toString(courseId));
        model.addAttribute("course", course);
        return "viewCourseInfo";
    }
    
    @RequestMapping(value = "view/{courseId}/attachment/{attachment:.+}",
            method = RequestMethod.GET)
    public View download(@PathVariable("courseId") long courseId,
                         @PathVariable("attachment") String name) {
        Course course = this.courseDatabase.get(courseId);
        if(course != null) {
            Attachment attachment = course.getAttachment(name);
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                           attachment.getMimeContentType(), attachment.getContents());
            }     
        }
        return new RedirectView("/classroom/listCourse", true);
    }
    
    @RequestMapping(value = "edit/{courseId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("courseId") long courseId,
            Principal principal, HttpServletRequest request) {
        Course course = this.courseDatabase.get(courseId);
        if (course == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(course.getCourseLecturer()))) {
            return new ModelAndView(new RedirectView("/classroom/listCourse", true));
        }

        ModelAndView modelAndView = new ModelAndView("editCourse");
        modelAndView.addObject("courseId", Long.toString(courseId));
        modelAndView.addObject("course", course);

        ClassroomController.CreateCourseForm courseForm = new ClassroomController.CreateCourseForm();
        courseForm.setCourseName(course.getCourseName());
        courseForm.setCourseDescription(course.getCourseDescription());
        modelAndView.addObject("courseForm", courseForm);

        return modelAndView;
    }
    
    @RequestMapping(value = "delete/{courseId}", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("courseId") long courseId) {
        if (this.courseDatabase.containsKey(courseId)) {
            this.courseDatabase.remove(courseId);
        }
        return "redirect:/classroom/listCourse";
    }
    
    @RequestMapping(value = "/{courseId}/delete/{attachment:.+}",method = RequestMethod.GET)
    public String deleteAttachment(@PathVariable("courseId") long courseId,
            @PathVariable("attachment") String name) {
        Course course = this.courseDatabase.get(courseId);
        if (course != null) {
            if (course.hasAttachment(name)) {
                course.deleteAttachment(name);
            }
        }
        return "redirect:/classroom/edit/" + courseId;
    }
    
    @RequestMapping(value = "/registerAccount", method = RequestMethod.GET)
    public ModelAndView showRegisterForm() {
        return new ModelAndView("registerAccount", "registerAccountForm", new RegisterAccountForm());
    }
    
    public static class CreateCourseForm {
        private String courseName;
        private String courseDescription;
        private String courseLecturer;
        private List<MultipartFile> attachments;

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseDescription() {
            return courseDescription;
        }

        public void setCourseDescription(String courseDescription) {
            this.courseDescription = courseDescription;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }
    
    public static class RegisterAccountForm {
        private String username;
        private String email;
        private String password;
        private String cPassword;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getcPassword() {
            return cPassword;
        }

        public void setcPassword(String cPassword) {
            this.cPassword = cPassword;
        }
        
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(CreateCourseForm form, Principal principal) throws IOException {
        Course course = new Course();
        course.setId(this.getNextCourseId());
        course.setCourseName(form.getCourseName());
        course.setCourseDescription(form.getCourseDescription());
        course.setCourseLecturer(principal.getName());
        
        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                && attachment.getContents() != null && attachment.getContents().length > 0) {
                course.addAttachment(attachment);
            }
        }
        this.courseDatabase.put(course.getId(), course);
        return new RedirectView("/classroom/view/" + course.getId(), true);
    }
    
    @RequestMapping(value = "registerAccount", method = RequestMethod.POST)
    public View registerAccount(RegisterAccountForm form) throws IOException {
        User newUser = new User();
        newUser.setUsername(form.getUsername());
        newUser.setPassword(form.getPassword());
        newUser.setcPassword(form.getcPassword());
        newUser.setEmail(form.getEmail());
        String pwd = newUser.getPassword();
        String cPwd = newUser.getcPassword();
        
        if (pwd.equals(cPwd)) {
            System.out.println("true");
            return new RedirectView("/classroom/login", true);
            //connect to database.
        }
        else {
            System.out.println("false");
            return new RedirectView("/classroom/lregisterAccount", true);
            //connect to database.
        }
    }
    
    @RequestMapping(value = "edit/{courseId}", method = RequestMethod.POST)
    public String edit(@PathVariable("courseId") long courseId, ClassroomController.CreateCourseForm form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        Course course = this.courseDatabase.get(courseId);
        if (course == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(course.getCourseLecturer()))) {
            return "redirect:/classroom/listCourse";
        }
        
        course.setCourseName(form.getCourseName());
        course.setCourseDescription(form.getCourseDescription());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                course.addAttachment(attachment);
            }
        }
        this.courseDatabase.put(course.getId(), course);
        return "redirect:/classroom/view/" + course.getId();
    }
    private synchronized long getNextCourseId() {
        return this.COURSE_ID_SEQUENCE++;
    }
}
