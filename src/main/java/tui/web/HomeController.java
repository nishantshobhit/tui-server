package tui.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import tui.dao.S3Service;
import tui.dao.TuiDao;
import tui.model.*;

@Controller
public class HomeController {

    @Autowired
    private TuiDao dao;

    @Autowired
    private S3Service s3service;

    static {
        Logger.getRootLogger().setLevel(Level.DEBUG);
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("home", Collections.singletonMap("name", "World"));
    }

    @RequestMapping(value="/tui/users/", method=RequestMethod.POST)
    @ResponseBody
    public User signup(@RequestBody Map<String, String> post) {
        return dao.createUser(post.get("email"), post.get("mobile"));
    }

    @RequestMapping("/tui/feed/")
    @ResponseBody
    public Feed getFeed(@RequestParam("q") String q) {
        if ("all".equals(q)) {
            return dao.getEntireFeed();
        } else {
            return dao.getFeed();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/tui/feed/items/", method=RequestMethod.POST)
    public void addToFeed(@RequestBody Map<String, String> body) {
        dao.insertItem(body.get("item"));
    }


    @RequestMapping(value="/tui/pictures/", method=RequestMethod.POST)
    @ResponseBody
    public Picture uploadPicture(@RequestParam("file") MultipartFile pic)
            throws IOException {
        return new Picture(s3service.store(pic.getInputStream(), pic.getContentType()));
    }

    @RequestMapping(value="/tui/reports/", method=RequestMethod.POST)
    @ResponseBody
    public Report createReport(@RequestBody Map<String, Object> post) {
        Map<String, String> loc = (Map<String, String>)post.get("location");
        String location = loc.get("latitude") + "," + loc.get("longitude");
        return dao.createReport((String)post.get("user_id"), (String) post.get("desc"), (String)post.get("picture_id"),
                location);
    }

    // TODO: ensure this does not insert twice
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/tui/spotfixes/{spotfix_id}/participants", method=RequestMethod.POST)
    public void participate(@PathVariable("spotfix_id") String spotfixId, @RequestBody Map<String, String> post) {
        dao.recordParticipation(post.get("user_id"), spotfixId);
    }

    @RequestMapping(value="/tui/users/{user_id}/spotfixes/")
    @ResponseBody
    public List<Spotfix> getMySpotfixes(@PathVariable("user_id") String userId) {
        return dao.getSpotFixesInvolvingMe(userId);
    }

    @RequestMapping(value="/tui/spotfixes/")
    @ResponseBody
    public List<Spotfix> getSpotfixes() {
        return dao.getAllSpotfixes();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/tui/spotfixes/{spotfix_id}", method=RequestMethod.POST)
    public void changeSpotfix(@PathVariable("spotfix_id") String spotfixId, @RequestBody Map<String, String> post) {
        dao.updateSpotfix(spotfixId, post.get("state"));
    }

    @RequestMapping(value="/tui/spotfixes/{spotfix_id}")
    @ResponseBody
    public Spotfix getSpotfix(@PathVariable("spotfix_id") String spotfixId) {
        return dao.getSpotfix(spotfixId);
    }

    @RequestMapping(value="/tui/reports/{report_id}")
    @ResponseBody
    public Report getReport(@PathVariable("report_id") String reportId) {
        return dao.getReport(reportId);
    }

    @RequestMapping(value="/tui/reports/")
    @ResponseBody
    public List<Report> getReports() {
        return dao.getAllReports();
    }

    @RequestMapping(value="/tui/reports/{report_id}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeReport(@PathVariable("report_id") String reportId, @RequestBody Map<String, String> post)
            throws ParseException {
        Date date = null;
        if (post.containsKey("scheduled_on")) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
            date = formatter.parse(post.get("scheduled_on"));
        }
        dao.updateReport(reportId, post.get("state"), date);
    }

    /*public static void main(String[] args) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        System.out.println(formatter.parse("11/01/2014 08:00 am"));
    } */
}
