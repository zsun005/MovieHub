package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.models.AddGenreReqeustModel;
import edu.uci.ics.zexis1.service.movies.models.BasicResponseModel;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeResponseModel;

public class AddGenre {
    public static BasicResponseModel addGenre(AddGenreReqeustModel reqeustModel, String email){
        String name = reqeustModel.getName();
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        BasicResponseModel responseModel;
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        if(resultCode != 140){
            responseModel = new BasicResponseModel(141);
            return responseModel;
        }
        if(AddMovie.genreExists(name)){
            responseModel = new BasicResponseModel(218);
            return responseModel;
        }
        int genreId = AddMovie.getNewGenreId(name);
        AddMovie.updateGenre(genreId, name);
        responseModel = new BasicResponseModel(217);
        return responseModel;
    }
}
