/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
import ouhk.comps380f.view.DownloadingView;

/**
 *
 * @author kenwong
 */
@Controller
@RequestMapping("classroom")
public class ClassroomController {
    private volatile long COURSE_ID_SEQUENCE = 1;
    private Map<Long, Course> courseDatabase = new Hashtable<>();
    
    @RequestMapping("/")
    public String index() {
        return "redirect:/classroom/listCourse";
    }
    
    @RequestMapping(value = {"", "listCourse"}, method = RequestMethod.GET)
    public String listCourse(ModelMap model) {
        model.addAttribute("courseDatabase", courseDatabase);
        return "listCourse";
    }
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("addCourse", "courseForm", new Form());
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
    
    public static class Form {
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

        public String getCourseLecturer() {
            return courseLecturer;
        }

        public void setCourseLecturer(String courseLecturer) {
            this.courseLecturer = courseLecturer;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(Form form) throws IOException {
        Course course = new Course();
        course.setId(this.getNextCourseId());
        course.setCourseName(form.getCourseName());
        course.setCourseDescription(form.getCourseDescription());
        course.setCourseLecturer(form.getCourseLecturer());
        
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
    
    private synchronized long getNextCourseId() {
        return this.COURSE_ID_SEQUENCE++;
    }
}
