package com.example.demo.controllers;

import com.example.demo.domain.model.projections.UserAdditionalPropections.UsernameUserProjection;
import com.example.demo.domain.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {

    private UserAdditionalService service;
    private GameService serviceG;
    private PlaceService servicePl;
    private GameResultService serviceGr;
    private GameTypeService serviceGt;
    private PlaceService placeService;
    private UserStatisticsService uss;
    private AuthService authService;

    private UserAdditionalService userAdditionalService;
    private GameMemberService gameMemberService;
    private EmailService emailService;

    public TestController(AuthService authService, UserAdditionalService service, GameMemberService serviceGM, GameService serviceG, PlaceService servicePl, GameResultService serviceGr, GameTypeService serviceGt, PlaceService placeService, UserStatisticsService uss, UserAdditionalService userAdditionalService, GameMemberService gameMemberService, EmailService emailService) {
        this.service = service;
        this.serviceG = serviceG;
        this.servicePl = servicePl;
        this.serviceGr = serviceGr;
        this.serviceGt = serviceGt;
        this.placeService = placeService;
        this.uss = uss;
        this.authService = authService;
        this.userAdditionalService = userAdditionalService;
        this.gameMemberService = gameMemberService;
        this.emailService = emailService;
    }


    @GetMapping("/statisticsTest")
    public ResponseEntity<?> TestStats()
    {
        var id= authService.GetAuthenticatedUserData().getId();
        return ResponseEntity.ok(uss.getByID(id));
    }

        @GetMapping("/getUserStatistics/{id}")
    public ResponseEntity<?> TestStats(@PathVariable int id)
    {
        return ResponseEntity.ok(uss.getByID(id));
    }

    @GetMapping("/getUserData")
    public ResponseEntity<?> GetUserData()
    {
        var entity = authService.GetAuthenticatedUserData();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/getUserData/{id}")
    public ResponseEntity<?> GetUserData(@PathVariable int id)
    {
        var entity = userAdditionalService.getByID(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/isUserLoggedIn")
    public ResponseEntity<?> CheckLoggedIn()
    {
        return ResponseEntity.ok(authService.checkIsUserAuthenticated());
    }

    @GetMapping("/token")
    public void passwordResetToken(@RequestParam("gmail") String gmail)
    {
        var token = userAdditionalService.activateToken(gmail);
        emailService.sendEmail(gmail, "Токен", "Ваш токен: " + token);
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<?> getInfoUser(@RequestParam("mail") String mail) {
        var user = userAdditionalService.getByEmail(mail);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/getLastGames")
    public ResponseEntity<?> getLastNGames()
    {
        var entity = authService.GetAuthenticatedUserData();
        var list = gameMemberService.getLastNGameMembersByUser(entity, PageRequest.of(0, 10));
        return ResponseEntity.ok(list);
    }






    @GetMapping("/getLastGames/{id}")
    public ResponseEntity<?> getLastNGames(@PathVariable int id)
    {
        var entity = userAdditionalService.getByID(id);
        if (entity.isPresent()) {
            var list = gameMemberService.getLastNGameMembersByUser(entity.get(), PageRequest.of(0, 10));
            return ResponseEntity.ok(list);
        }
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<?> getGameDetails(@PathVariable int id)
    {
        var entity = serviceG.getByIDWithMembers(id);
        if(entity.isPresent()) {
            return ResponseEntity.ok(entity);
        }
        else
        {
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("/archive")
    public ResponseEntity<?> getGames(@RequestParam("page") int page, @RequestParam("count") int count)
    {
        var list = serviceG.getAllGamesArhived(PageRequest.of(page,count));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/archive/test")
    public ResponseEntity<?> getGamestest(@RequestParam(name = "day", required = false) Integer day,
                                          @RequestParam(name="month", required = false) Integer month,
                                          @RequestParam(name="year", required = false) Integer year,
                                          @RequestParam(name="typeId", required = false) Integer typeId,
                                          @RequestParam(name="placeId", required = false) Integer placeId,
                                          @RequestParam(name="gameResult", required = false) Integer gameResultId,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("count") Integer count)
    {
        var list = serviceG.getAllGamesArchivedByDate(day, month, year, typeId, placeId, gameResultId, PageRequest.of(page,count));
        return ResponseEntity.ok(list);
    }



    @GetMapping("/gameTypes")
    public ResponseEntity<?> getGameTypes()
    {
        var entity = serviceGt.getAll();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/gameResults")
    public ResponseEntity<?> getGameResults()
    {
        var entity = serviceGr.getAll();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/gamePlaces")
    public ResponseEntity<?> getGamePlaces()
    {
        var entity = placeService.getAll();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam(name = "searchString", required = false) String searchString,
                                      @RequestParam("page") Integer page,
                                      @RequestParam("count") Integer count)
    {
        var entity = userAdditionalService.getAllBySearchString(searchString, PageRequest.of(page, count), UsernameUserProjection.class);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/ratings")
    public ResponseEntity<?> getRatings(@RequestParam(name = "searchString", required = false) String searchString,
                                      @RequestParam("page") Integer page,
                                      @RequestParam("count") Integer count)
    {
        var entity = uss.getAllShort(searchString, PageRequest.of(page, count, Sort.by("rank").ascending()));
        return ResponseEntity.ok(entity);
    }








}
