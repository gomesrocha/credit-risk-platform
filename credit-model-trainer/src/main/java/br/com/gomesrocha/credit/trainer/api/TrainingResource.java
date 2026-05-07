package br.com.gomesrocha.credit.trainer.api;

import br.com.gomesrocha.credit.trainer.api.dto.TrainingRequest;
import br.com.gomesrocha.credit.trainer.api.dto.TrainingResponse;
import br.com.gomesrocha.credit.trainer.domain.service.TrainingService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/training")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrainingResource {

    @Inject
    TrainingService trainingService;

    @POST
    public TrainingResponse train(@Valid TrainingRequest request) {
        return trainingService.train(request);
    }

    @GET
    @Path("/health")
    public String health() {
        return "credit-model-trainer is running";
    }
}