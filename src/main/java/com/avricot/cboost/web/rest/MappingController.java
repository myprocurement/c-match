package com.avricot.cboost.web.rest;

import com.avricot.cboost.service.project.ProjectMappingJson;
import com.avricot.cboost.service.project.MappingService;
import com.codahale.metrics.annotation.Timed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;

/**
 *
 */
@Controller
public class MappingController {

    @Inject
    private MappingService mappingService;

    @RequestMapping(value = "/rest/project/{projectId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @Timed
    public ProjectMappingJson getProjectMapping(@PathVariable Long projectId) throws IOException {
        return mappingService.getById(projectId);
    }

    @RequestMapping(value = "/rest/project", method = RequestMethod.POST, produces = "application/json")
    @Timed
    public void updateProjectMapping(@RequestBody ProjectCommand command) throws IOException {
        mappingService.updateProjectMapping(command);
    }

}
