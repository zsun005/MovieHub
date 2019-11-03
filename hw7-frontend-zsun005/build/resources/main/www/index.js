let sessionID;
let email;
let notFirst = 0;
let global_limit=10;
let global_page=1;
let currentUrlWithMovieInfo;
let current_movieId;
let updateAmount;

function getCookie(cname){
    var name = email + "=";
    var ca = document.cookie.split(";");
    for (var i=0; i<ca.length; ++i){
        var c = ca[i];
        while (c.charAt(0) == ' '){
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0){
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

$("#HomeBtn").click(function(event){
    event.preventDefault();

    let regionDom = $("#region");
    regionDom.empty();

    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();
});

$("#quickSearchBtn").click(function(event){
    event.preventDefault();
    let title = $("#quickSearchInput").val();
    let url = "http://0.0.0.0:6211/api/g/movies/search?";
    url=addUrl("title", title, url);
    $.ajax({
        url: url,
        contentType: "application/json",
        type: "GET",
        headers: {
            "Access-Control-Expose-Headers": "*",
            "Access-Control-Allow-Origin": "*",
            "sessionID": sessionID,
            "email": email
        },
        crossDomain: true,
        statusCode: {
            204: handleQuickSearchSuccess,
            400: handleBadRequest,
            500: handleInternalServerError
        }
    });
});

$("#checkOutBtn").click(function(event){
    event.preventDefault();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();
    let placeOrderRequestModel = {email: email};

    $.ajax({
        url: "http://0.0.0.0:6211/api/g/billing/order/place",
        contentType: "application/json",
        type: "POST",
        data: JSON.stringify(placeOrderRequestModel),
        headers: {
            "Access-Control-Expose-Headers": "*",
            "Access-Control-Allow-Origin": "*",
            "email": email,
            "sessionID": sessionID
        },
        crossDomain: true,
        statusCode: {
            204: handlePlaceOrderSuccess,
            400: handleBadRequest,
            500: handleInternalServerError
        }
    });
});

function handlePlaceOrderSuccess(response, status, xhr){
    console.log("inside advanced search api success...");
    let transactionID = xhr.getResponseHeader("transactionID");
    console.log("transactionID : " + transactionID);
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportPlaceOrderSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportPlaceOrderSuccess(response){
    let resultCode = response["resultCode"];
    if (resultCode !== 3400){
        alert(response.message);
        console.log(response.message);
    }else{
        let regionDOM = $("#region");
        regionDOM.empty();
        let paymentURL = response["redirectURL"];

        let paymentButton = "<br><br><button id='makePaymentButton' onclick=\"window.open(\'"+paymentURL+"\')\">Complete Payment in Paypal</button>";
        regionDOM.append(paymentButton);
    }
}

function handleQuickSearchSuccess(response, status, xhr){
    console.log("inside advanced search api success...");
    let transactionID = xhr.getResponseHeader("transactionID");
    console.log("transactionID : " + transactionID);
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportSearchSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

$("#loginBtn").click(function(event){
    event.preventDefault();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();

    let regionDom = $("#region");
    regionDom.empty();

    console.log("empty Dom ...");
    let html = "<form class=\"modal-content animate\" action=\"#\">\n" +
        "        <div class=\"container\">\n" +
        "            <label for=\"login_email\"><b>Email</b></label>\n" +
        "            <input type=\"email\" placeholder=\"Enter Email\" name=\"email\" required id=\"login_email\">\n" +
        "            <label for=\"login_password\"><b>Password</b></label>\n" +
        "            <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required id=\"login_password\">\n" +
        "            <button  id=\"sendLoginRequest\" type=\"submit\">Login</button>\n" +
        "        </div>\n" +
        "    </form>";

    regionDom.append(html);

    $("#sendLoginRequest").click(function(event) {
        event.preventDefault();
        console.log("clicked sendLoginRequest...");
        console.log("sending login request ...");

        let email = $("#login_email").val();
        let password = $("#login_password").val();

        console.log("email: "+email);
        console.log("password: "+password);

        let loginRequestModel = {email: email, password: password};

        $.ajax({
            url: "http://0.0.0.0:6211/api/g/idm/login",
            contentType: "application/json",
            type: "POST",
            data: JSON.stringify(loginRequestModel),
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*"
            },
            crossDomain: true,
            statusCode: {
                204: handleloginSuccess,
                400: handleBadRequest,
                500: handleInternalServerError
            }
        });

    });
});

$("#browseBtn").click(function(event){
    event.preventDefault();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();
    let error = $("#region");
    error.empty();
    console.log("clicked browse movie ...");
    $.ajax({
        url: "http://0.0.0.0:6211/api/g/movies/genre",
        type: "GET",
        contentType: "application/json",
        headers: {
            "Access-Control-Expose-Headers": "*",
            "Access-Control-Allow-Origin": "*",
            "email": email,
            "sessionID": sessionID
        },
        crossDomain: true,
        //success: reportRetrieveCartSuccess,
        statusCode: {
            204: handleGetGenres,
            400: handleBadRequest,
            500: handleInternalServerError
        }
    });
});

function handleGetGenres(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportGetGenresSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportGetGenresSuccess(response){
    if(response.resultCode != 219){
        alert(response.message);
        console.log(response.message);
    }else{
        let genresBarDOM = $("#genresBar");
        genresBarDOM.empty();

        let genres = response.genres;
        let html = "<table style=\"width:100%\">";
        for(let i=0; i<genres.length; ++i){
            let genreName = genres[i].name;
            console.log("genre name: "+genreName);
            html += "<tr>" +
                "<tb>" + genreName + " </tb>" +
                "</tr>";
        }
        html += "</table>";

        genresBarDOM.append(html);

        $(".searchByGenre").click(function (event) {
            event.preventDefault();
            let genreName = this.id;
            console.log("genreName: "+genreName);

            $.ajax({
                url: "http://0.0.0.0:6211/api/g/movies/search?genre="+genreName,
                contentType: "application/json",
                type: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "sessionID": sessionID,
                    "email": email
                },
                crossDomain: true,
                statusCode: {
                    204: handleSearchSuccess,
                    400: handleBadRequest,
                    500: handleInternalServerError
                }
            });
        });
    }
}

$("#cartBtn").click(function (event) {
    event.preventDefault();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();
    let retrieveCartRequestModel={email: email};

    $.ajax({
        url: "http://0.0.0.0:6211/api/g/billing/cart/retrieve",
        contentType: "application/json",
        type: "POST",
        data: JSON.stringify(retrieveCartRequestModel),
        headers: {
            "Access-Control-Expose-Headers": "*",
            "Access-Control-Allow-Origin": "*",
            "email": email,
            "sessionID": sessionID
        },
        crossDomain: true,
        statusCode: {
            204: handleRetrieveCartSuccess,
            400: handleBadRequest,
            500: handleInternalServerError
        }
    });
});

function handleRetrieveCartSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportRetrieveCartSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportRetrieveCartSuccess(response){
    let items = response.items;
    alert(response.message);
    let regionDom = $("#region");
    regionDom.empty();

    let rowHTML = "<table border=\"1\"><tr><td>Movie ID</td><td>Quantity</td>";

    for (let i = 0; i<items.length; ++i){
        rowHTML += "<tr>";
        let itemObj = items[i];
        //"<button type=\"submit\" id=\"detailOf\""+ movieObject["movieId"] +"\">Search</button>";
        //let detailDtn = "<button class='movieDetail' type='submit' id='detailOf"+ movieObject["movieId"] +"'>"+movieObject["movieId"]+"</button>";//=  "<button type=\"submit\" id=\"detailOf\""+movieObject["title"]>movieObject["title"]+"</button>";
        rowHTML += "<td>" + itemObj["movieId"] + "</td>";
        rowHTML += "<td>" + itemObj["quantity"] + "</td>";
        rowHTML += "</tr>";
    }
    rowHTML += "</table>";
    regionDom.append(rowHTML);
}

$("#orderHistoryBtn").click(function(event){
    event.preventDefault();
    let retrieveOrderRequestModel={email: email};
    let regionDom = $("#region");
    regionDom.empty();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();
    $.ajax({
        url: "http://0.0.0.0:6211/api/g/billing/order/retrieve",
        contentType: "application/json",
        type: "POST",
        data: JSON.stringify(retrieveOrderRequestModel),
        headers: {
            "Access-Control-Expose-Headers": "*",
            "Access-Control-Allow-Origin": "*",
            "email": email,
            "sessionID": sessionID
        },
        crossDomain: true,
        statusCode: {
            204: handleRetrieveOrderSuccess,
            400: handleBadRequest,
            500: handleInternalServerError
        }
    });
});

function handleRetrieveOrderSuccess(response, status, xhr) {
    let transactionID = xhr.getResponseHeader("transactionID");
    console.log("TransactionID: " + transactionID);
    sleep(6000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportRetrieveOrderSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportRetrieveOrderSuccess(response, status, xhr){
    if (response.resultCode != 3410){
        alert(response.message);
        console.log(response.message);
    }else {
        let transactions = response.transactions;
        console.log("Type of Items: " + typeof(transactions));
        alert(response.message);
        let regionDom = $("#region");
        regionDom.empty();

        let rowHTML = "<table border=\"1\"><tr><td>Email</td><td>Movie ID</td><td>Quantity</td><td>Sale Date</td>";

        for (i in response.transactions) {
            for(j in response.transactions[i].items) {
                rowHTML += "<tr>";
                let itemObj = response.transactions[i].items[j];

                rowHTML += "<td>" + itemObj["email"] + "</td>";
                rowHTML += "<td>" + itemObj["movieId"] + "</td>";
                rowHTML += "<td>" + itemObj["quantity"] + "</td>";
                rowHTML += "<td>" + itemObj["saleDate"] + "</td>";
                rowHTML += "</tr>";
            }
        }
        rowHTML += "</table>";
        regionDom.append(rowHTML);
    }
}

$("#registerBtn").click(function(event){
    event.preventDefault();
    let regionDom = $("#region");
    regionDom.empty();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();

    let html = "<form class=\"modal-content animate\" action=\"/action_page.php\">\n" +
        "        <div class=\"container\">\n" +
        "            <label for=\"register_email\"><b>Email</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter Email\" name=\"email\" required id=\"register_email\">\n" +
        "            <label for=\"register_password\"><b>Password</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter Password\" name=\"password\" required id=\"register_password\">\n" +
        "            <button id=\"sendRegisterRequest\" type=\"submit\">Register</button>\n" +
        "        </div>\n" +
        "    </form>";

    regionDom.append(html);

    $("#sendRegisterRequest").click(function(event) {
        event.preventDefault();

        let email = $("#register_email").val();
        let password = $("#register_password").val();

        let loginRequestModel = {email: email, password: password};

        $.ajax({
            url: "http://0.0.0.0:6211/api/g/idm/register",
            contentType: "application/json",
            type: "POST",
            data: JSON.stringify(loginRequestModel),
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*"
            },
            crossDomain: true,
            statusCode: {
                204: handleRegisterSuccess,
                400: handleBadRequest,
                500: handleInternalServerError
            }
        });

    });
});

$("#advancedSearchBtn").click(function(event){
    event.preventDefault();

    let regionDom = $("#region");
    regionDom.empty();
    let genresBarDOM = $("#genresBar");
    genresBarDOM.empty();

    let html = "<form class=\"modal-content animate\" action=\"/action_page.php\">\n" +
        "        <div class=\"container\">\n" +
        "            <label for=\"title\"><b>Movie Title</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter Movie Title\" name=\"title\" required id=\"advanced_title\">\n" +
        "            <hr>\n" +
        "            <label for=\"year\"><b>Year</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter Year\" name=\"year\" required id=\"advanced_year\">\n" +
        "            <hr>\n" +
        "            <label for=\"director\"><b>Director</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter The Director\" name=\"director\" required id=\"advanced_director\">\n" +
        "            <hr>\n" +
        "            <label for=\"genre\"><b>Genre</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter The Genre\" name=\"genre\" required id=\"advanced_genre\">\n" +
        "            <hr>\n" +
        "            <n></n><label for=\"limit\"><b>Limit</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter The Limit\" name=\"limit\" required id=\"advanced_limit\">\n" +
        "            <hr>\n" +
        "            <label for=\"page\"><b>Page</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter The Offset\" name=\"offset\" required id=\"advanced_page\">\n" +
        "            <hr>\n" +
        "            <label for=\"orderby\"><b>Orderby</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter 'rating' or 'title' \" name=\"orderby\" required id=\"advanced_orderby\">\n" +
        "            <hr>\n" +
        "            <label for=\"direction\"><b>Direction</b></label>\n" +
        "            <input type=\"text\" placeholder=\"Enter 'asc' or 'desc'\" name=\"direction\" required id=\"advanced_direction\">\n" +
        "            <hr>\n" +
        "            <button type=\"submit\" id=\"sendAdvancedSearchRequest\">Search</button>\n" +
        "        </div>\n" +
        "    </form>";

    regionDom.append(html);

    $("#sendAdvancedSearchRequest").click(function(event){
            event.preventDefault();

            let title = $("#advanced_title").val();
            let year = $("#advanced_year").val();
            let director = $("#advanced_director").val();
            let genre = $("#advanced_genre").val();
            let limit = $("#advanced_limit").val();
            let page = $("#advanced_page").val();
            let orderby = $("#advanced_orderby").val();
            let direction = $("#advanced_direction").val();

            let offset;
            if (limit){
                global_limit  = limit;
            }
            if (page){
                global_page = page;
                offset = (global_page-1)*global_limit;
            }

            let url = "http://0.0.0.0:6211/api/g/movies/search?";
            url=addUrl("title", title, url);
            url=addUrl("year", year, url);
            url=addUrl("director", director, url);
            url=addUrl("genre", genre, url);
            currentUrlWithMovieInfo = url;
            url=addUrl("limit", limit, url);
            url=addUrl("offset", offset, url);
            url=addUrl("orderby", orderby, url);
            url=addUrl("direction", direction, url);

            $.ajax({
                url: url,
                contentType: "application/json",
                type: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "sessionID": sessionID,
                    "email": email
                },
                crossDomain: true,
                statusCode: {
                    204: handleSearchSuccess,
                    400: handleBadRequest,
                    500: handleInternalServerError
                }
            });
        }
    );
});

function addUrl(name, input, url){
    if (input){
        if (notFirst == 0) {
            url += name+"="+ input;
            notFirst = 1;
        }else{
            url += "&"+name+"=" + input;
        }
    }

    return url;
}

$("#cartBtn").click(function(event){
    event.preventDefault();

    let regionDom = $("#region");
    regionDom.empty();
});

function handleloginSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    console.log("Login Success transactionID: " + transactionID)
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                },
                success: reportLoginSuccess
            });
        }
    )
}

function reportNoContent(response, xhr) {
    let transactionID = xhr.getResponseHeader("transactionID");
    console.log("transactionID : " + transactionID);
    alert("No such transactionID: "+transactionID);

    let regionDom = $("#region");
    regionDom.empty();
}

function handleRegisterSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportRegisterSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function handleSearchSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportSearchSuccessWithPagination,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}
function reportSearchSuccess(response,status, xhr){
    if (response.resultCode == 210){
        alert(response.message);
        let regionDom = $("#region");
        regionDom.empty();

        let rowHTML = "<table border=\"1\"><tr><td>Movie ID</td><td>Title</td><td>Director</td><td>Year</td>";
        let movies = response.movies;

        for (let i = 0; i<movies.length; ++i){
            rowHTML += "<tr>";
            let movieObject = movies[i];

            //"<button type=\"submit\" id=\"detailOf\""+ movieObject["movieId"] +"\">Search</button>";
            let detailDtn = "<button class='movieDetail' type='submit' id='detailOf"+ movieObject["movieId"] +"'>"+movieObject["movieId"]+"</button>";//=  "<button type=\"submit\" id=\"detailOf\""+movieObject["title"]>movieObject["title"]+"</button>";
            rowHTML += "<td>" + detailDtn + "</td>";
            rowHTML += "<td>" + movieObject["title"] + "</td>";
            rowHTML += "<td>" + movieObject["director"] + "</td>";
            rowHTML += "<td>" + movieObject["year"] + "</td>";
            rowHTML += "</tr>";
        }
        rowHTML += "</table>";
        regionDom.append(rowHTML);

        $(".movieDetail").click(function(event){
            event.preventDefault();
            let id = this.id;
            let movieId = id.substring(8, 17);
            console.log("button id: "+id+"; movieId: "+movieId);

            $.ajax({
                url: "http://0.0.0.0:6211/api/g/movies/get/"+movieId,
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                //success: ,
                statusCode: {
                    204: handleMovieDetail,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        });
    }else{
        alert(response.message);
    }
}

function handleMovieDetail(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportMovieDetail,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportMovieDetail(response) {
    if (response.resultCode != 210){
        alert("No such movie.");
        console.log("Can't get movie by its id...");
    }else{
        let movie = response.movie;
        let movieId = movie.movieId;
        current_movieId = movieId;
        let title = movie.title;
        let director = movie.director;
        let year = movie.year;
        let backdrop_path = movie.backdrop_path;
        let overview = movie.overview;
        let poster_path = movie.poster_path;
        let revenue = movie.revenue;
        let rating = movie.rating;
        let numVotes = movie.numVotes;
        let genres = movie.genres;
        let stars = movie.stars;

        let html = generateMovieDetailHTML(movieId, title, director, year, backdrop_path, overview, poster_path, revenue, rating, numVotes, genres, stars);

        html +="<hr>" +
            "<label>Update Rating</label>\n" +
            "<input type=\"text\" placeholder=\"Enter Your Rating (0 to 10)\" name=\"newRating\" required id=\"updateRating\"><button type=\"submit\" id=\"updateRatingBtn\">Update</button>\n"+
            "<hr>" +
            "<label>Quantity</label>\n" +
            "<input type=\"text\" placeholder=\"Enter The Quantity You Want\" name=\"quantity\" required id=\"quantity\"><button type=\"submit\" id=\"AddToCartBtn\">Add To Cart</button>\n";

        let regionDom = $("#region");
        regionDom.empty();

        regionDom.append(html);

        $("#updateRatingBtn").click(function(event){
            event.preventDefault();
            let newRating = $("#updateRating").val();
            let updateRatingRequestModel = {id: movieId, rating: newRating};

            $.ajax({
                url: "http://0.0.0.0:6211/api/g/movies/rating",
                contentType: "application/json",
                type: "POST",
                data: JSON.stringify(updateRatingRequestModel),
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                statusCode: {
                    204: handleUpdateRatingSuccess,
                    400: handleBadRequest,
                    500: handleInternalServerError
                }
            });
        });

        $("#AddToCartBtn").click(function(event){
            event.preventDefault();
            let quantity = $("#quantity").val();
            updateAmount = quantity;
            let insertCartRequestModel = {email: email, movieId: movieId, quantity: quantity};

            $.ajax({
                url: "http://0.0.0.0:6211/api/g/billing/cart/insert",
                contentType: "application/json",
                type: "POST",
                data: JSON.stringify(insertCartRequestModel),
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                statusCode: {
                    204: handleInsertCartSuccess,
                    400: handleBadRequest,
                    500: handleInternalServerError
                }
            });
        });
    }
}

function handleInsertCartSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportInsertCartSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportInsertCartSuccess(response, status, xhr){
    if (response.resultCode == 311){
        let updateCartRequestModel = {email: email, movieId: current_movieId, quantity: updateAmount};

        $.ajax({
            url: "http://0.0.0.0:6211/api/g/billing/cart/update",
            contentType: "application/json",
            type: "POST",
            data: JSON.stringify(updateCartRequestModel),
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*",
                "email": email,
                "sessionID": sessionID
            },
            crossDomain: true,
            statusCode: {
                204: handleUpdateCartSuccess,
                400: handleBadRequest,
                500: handleInternalServerError
            }
        });
    }else {
        alert(response.message);
        console.log(response.message);
    }
}

function handleUpdateCartSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportUpdateCartSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportUpdateCartSuccess(response) {
    alert(response.message);
    console.log(response.message);
}

function handleUpdateRatingSuccess(response, status, xhr){
    let transactionID = xhr.getResponseHeader("transactionID");
    sleep(2000).then(()=> {
            $.ajax({
                url: "http://0.0.0.0:6211/api/g/report",
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID
                },
                crossDomain: true,
                success: reportUpdateRatingSuccess,
                statusCode: {
                    204: reportNoContent,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        }
    )
}

function reportUpdateRatingSuccess(response){
    alert(response.message);
    console.log(response.message);
}

function generateMovieDetailHTML(movieId, title, director, year, backdrop_path, overview, poster_path, revenue, rating, numVotes, genres, stars) {
    let html = "";
    if (movieId){html+="MovieId: "+movieId+"\n<hr>";}
    if (title){html+="Title: "+title+"\n<hr>";}
    if (director){html+="Director: "+director+"\n<hr>";}
    if (year){html+="Year: "+year+"\n<hr>";}
    if (backdrop_path){html+="Backdrop_path: "+backdrop_path+"\n<hr>";}
    if (overview){html+="Overview: "+overview+"\n<hr>";}
    if (poster_path){html+="Poster_path: "+poster_path+"\n<hr>";}
    if (revenue){html+="Revenue: "+revenue+"\n<hr>";}
    if (rating){html+="Rating: "+rating+"\n<hr>";}
    if (numVotes){html+="NumVotes: "+numVotes+"\n<hr>";}
    if (genres){
        html+="Genres: \n" + genres + "\n<hr>";
    }
    if(stars){
        html += "Stars: \n" + stars + "\n<hr>";
    }

    console.log(html);

    return html;
}

function reportSearchSuccessWithPagination(response,status, xhr){
    if (response.resultCode == 210){
        alert(response.message);
        let regionDom = $("#region");
        regionDom.empty();

        let movies = response.movies;
        let rowHTML = "<div class='advancedSettingClass'>" +
            "            <label for=\"limit\"><b>Limit</b></label>\n" +
            "            <input type=\"text\" placeholder=\"Enter The Limit\" name=\"limit\" required id=\"advanced_limit\">\n" +
            "            <hr>\n" +
            "            <label for=\"page\"><b>Page</b></label>\n" +
            "            <input type=\"text\" placeholder=\"Enter The Offset\" name=\"offset\" required id=\"advanced_page\">\n" +
            "            <hr>\n" +
            "            <label for=\"orderby\"><b>Orderby</b></label>\n" +
            "            <input type=\"text\" placeholder=\"Enter 'rating' or 'title' \" name=\"orderby\" required id=\"advanced_orderby\">\n" +
            "            <hr>\n" +
            "            <label for=\"direction\"><b>Direction</b></label>\n" +
            "            <input type=\"text\" placeholder=\"Enter 'asc' or 'desc'\" name=\"direction\" required id=\"advanced_direction\">\n" +
            "            <hr>\n" +
            "            <button type=\"submit\" id=\"sendAdvancedSearchRequest\">Search</button>\n"+
            "          </div>"+
            "<table border=\"1\"><tr><td>Movie ID</td><td>Title</td><td>Director</td><td>Year</td>";

        for (let i = 0; i < movies.length; ++i) {
            rowHTML += "<tr>";
            let movieObject = movies[i];

            let detailDtn = "<button class='movieDetail' type='submit' id='detailOf"+ movieObject["movieId"] +"'>"+movieObject["movieId"]+"</button>";//=  "<button type=\"submit\" id=\"detailOf\""+movieObject["title"]>movieObject["title"]+"</button>";
            rowHTML += "<td>" + detailDtn + "</td>";
            rowHTML += "<td>" + movieObject["title"] + "</td>";
            rowHTML += "<td>" + movieObject["director"] + "</td>";
            rowHTML += "<td>" + movieObject["year"] + "</td>";
            rowHTML += "</tr>";
        }
        rowHTML += "</table>";
        regionDom.append(rowHTML);
        $(".movieDetail").click(function(event){
            event.preventDefault();
            let id = this.id;
            let movieId = id.substring(8, 17);
            console.log("button id: "+id+"; movieId: "+movieId);

            $.ajax({
                url: "http://0.0.0.0:6211/api/g/movies/get/"+movieId,
                type: "GET",
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                //success: ,
                statusCode: {
                    204: handleMovieDetail,
                    400: reportBadRequest,
                    500: reportInternalServerError
                }
            });
        });

        $("#sendAdvancedSearchRequest").click(function(event){
            event.preventDefault();

            let limit = $("#advanced_limit").val();
            let page = $("#advanced_page").val();
            let orderby = $("#advanced_orderby").val();
            let direction = $("#advanced_direction").val();

            let offset;
            if (limit){
                global_limit  = limit;
            }
            if (page){
                global_page = page;
                offset = (global_page-1)*global_limit;
            }

            let url = currentUrlWithMovieInfo;
            url=addUrl("limit", limit, url);
            url=addUrl("offset", offset, url);
            url=addUrl("orderby", orderby, url);
            url=addUrl("direction", direction, url);

            $.ajax({
                url: url,
                contentType: "application/json",
                type: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "sessionID": sessionID,
                    "email": email
                },
                crossDomain: true,
                statusCode: {
                    204: handleSearchSuccess,
                    400: handleBadRequest,
                    500: handleInternalServerError
                }
            });
        });
    }else{
        alert(response.message);
    }
}

function reportRegisterSuccess(response) {
    if(response["resultCode"]!=110){
        alert(response.message);
    }
    else {
        alert(response.message);
        let regionDom = $("#region");
        regionDom.empty();

        let html = "<form class=\"modal-content animate\" action=\"#\">\n" +
            "        <div class=\"container\">\n" +
            "            <label for=\"login_email\"><b>Email</b></label>\n" +
            "            <input type=\"email\" placeholder=\"Enter Email\" name=\"email\" required id=\"login_email\">\n" +
            "            <label for=\"login_password\" type = \"password\"><b>Password</b></label>\n" +
            "            <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required id=\"login_password\">\n" +
            "            <button  id=\"sendLoginRequest\" type=\"submit\">Login</button>\n" +
            "        </div>\n" +
            "    </form>";

        regionDom.append(html);

        $("#sendLoginRequest").click(function(event) {
            event.preventDefault();

            let email = $("#login_email").val();
            let password = $("#login_password").val();
            let loginRequestModel = {email: email, password: password};

            $.ajax({
                url: "http://0.0.0.0:6211/api/g/idm/login",
                contentType: "application/json",
                type: "POST",
                data: JSON.stringify(loginRequestModel),
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*"
                },
                crossDomain: true,
                statusCode: {
                    204: handleloginSuccess,
                    400: handleBadRequest,
                    500: handleInternalServerError
                }
            });

        });
    }
}

const sleep = (milliseconds) => {
    return new Promise(resolve => setTimeout(resolve, milliseconds))
}

function reportBadRequest(){
    alert("Sorry, Website error!");
    console.log("report endpoint returned bad request!");
}

function reportInternalServerError(){
    alert("Sorry, Website error!");
    console.log("report endpoint occurred internal server error!");
}

function reportLoginSuccess(response){


    if(response["resultCode"]!=120){
        alert(response.message);
    }
    else {
        alert("Congratulations! Login successfully!");
        sessionID = response.sessionID;
        let user_email= $("#login_email").val();
        email = user_email;
        let regionDom = $("#region");
        regionDom.empty();
    }
}

function handleBadRequest(){
    alert("400");
    console.log("Received BadRequest from apiGateWay!");
}

function handleInternalServerError(){
    alert("500");
    console.log("Received Internal Server Error from apiGateWay!");
}