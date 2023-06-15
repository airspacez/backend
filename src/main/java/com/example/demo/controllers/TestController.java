package com.example.demo.controllers;

import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UsernameUserWithRatingProjection;
import com.example.demo.domain.service.AnaliticsService;
import com.example.demo.domain.model.Club;
import com.example.demo.domain.model.CompositeKeys.UserInEventPK;
import com.example.demo.domain.model.Place;
import com.example.demo.domain.model.projections.EventProjections.EventWithUserUsernameList;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UsernameUserProjection;
import com.example.demo.domain.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@RestController
public class TestController {

    private UserAdditionalService service;
    private final GameService gameService;
    private PlaceService servicePl;
    private final GameResultService serviceGr;
    private final GameTypeService serviceGt;
    private final PlaceService placeService;
    private final UserStatisticsService uss;
    private final AuthService authService;
    private final CityService cityService;
    private final ClubRoleService roleService;
    private final ClubService clubService;
    private final EventService eventService;

    private final UserInEventService userInEventService;
    private final AnaliticsService analiticsService;

    private final UserAdditionalService userAdditionalService;
    private final GameMemberService gameMemberService;
    private final EmailService emailService;
    private final ClubStatusService clubStatusService;

    private final StatisticsOfUserService statisticsOfUserService;

    public TestController(AuthService authService, UserAdditionalService service, GameMemberService serviceGM, GameService serviceG, PlaceService servicePl, GameResultService serviceGr, GameTypeService serviceGt, PlaceService placeService, UserStatisticsService uss, CityService cityService, ClubRoleService roleService, ClubService clubService, EventService eventService, UserInEventService userInEventService, AnaliticsService analiticsService, UserAdditionalService userAdditionalService, GameMemberService gameMemberService, EmailService emailService, ClubStatusService clubStatusService, StatisticsOfUserService statisticsOfUserService) {
        this.service = service;
        this.gameService = serviceG;
        this.servicePl = servicePl;
        this.serviceGr = serviceGr;
        this.serviceGt = serviceGt;
        this.placeService = placeService;
        this.uss = uss;
        this.authService = authService;
        this.cityService = cityService;
        this.roleService = roleService;
        this.clubService = clubService;
        this.eventService = eventService;
        this.userInEventService = userInEventService;
        this.analiticsService = analiticsService;
        this.userAdditionalService = userAdditionalService;
        this.gameMemberService = gameMemberService;
        this.emailService = emailService;
        this.clubStatusService = clubStatusService;
        this.statisticsOfUserService = statisticsOfUserService;
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
    public ResponseEntity<?> CheckLoggedIn() {
        return ResponseEntity.ok(authService.checkIsUserAuthenticated());
    }

    @GetMapping("/userRole")
    public ResponseEntity<?> getUserRole() {
        return ResponseEntity.ok(authService.GetAuthenticatedUserData().getRole());
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<?> getInfoUser(@RequestParam("mail") String mail) {
        var user = userAdditionalService.getByEmail(mail);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/token")
    public void passwordResetToken(@RequestParam("mail") String mail)
    {
        var token = userAdditionalService.activateToken(mail);
        emailService.sendEmail(mail, "Токен", "Ваш токен: " + token);
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken(
            @RequestParam(value = "mail", required = true) String mail,
            @RequestParam(value = "token", required = true) String token) {

        String tokenFromDB = userAdditionalService.getTokenByEmail(mail);
        if (tokenFromDB != null && tokenFromDB.equals(token)) {
            System.out.println("HAVE TOKEN!!!");
            return ResponseEntity.ok("Token is valid!");
        }
        System.out.println("NOT HAVE TOKEN!!!");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/changePassword")
    public void changePassword(@RequestParam("password") String password, @RequestParam("UserEmail") String UserEmail)
    {
        userAdditionalService.changePassword(password, UserEmail);
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
        var entity = gameService.getByIDWithMembers(id);
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
        var list = gameService.getAllGamesArhived(PageRequest.of(page,count));
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
        var list = gameService.getAllGamesArchivedByDate(day, month, year, typeId, placeId, gameResultId, PageRequest.of(page,count));
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


    @GetMapping("/clubs")
    public ResponseEntity<?> getClubs()
    {
        var entity = clubService.getAllWithCity();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/clubs/{id}")
    public ResponseEntity<?> getClubs(@PathVariable int id)
    {
        var entity = clubService.getByIdWithCity(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/statuses")
    public ResponseEntity<?> getStatuses()
    {
        var entity = clubStatusService.getAll();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles()
    {
        var entity = roleService.getAll();
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> updates)
    {
        try {
            System.out.println("11!!!!!!");
            System.out.println(updates);
            userAdditionalService.createUser(updates);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("22222!!!!!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> patchUserData(@PathVariable int id, @RequestBody Map<String, Object> updates)
    {
        try {
                userAdditionalService.patch(id, updates);
                return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }


    @PatchMapping("/usersInEvents/{eventId}/tableNo/{tableNo}")
    public ResponseEntity<?> setleadToTableonevent(@PathVariable("eventId") int id, @PathVariable("tableNo") int tableNo,  @RequestBody Map<String, Object> updates)
    {
        try {
            userInEventService.setLeadToTableOnEvent(id, tableNo, updates);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @PatchMapping("/clubs/{id}")
    public ResponseEntity<?> patchClubData(@PathVariable int id, @RequestBody Map<String, Object> updates)
    {
        try {
            clubService.patch(id, updates);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @GetMapping("clubs/{id}/hasAnyUsers")
    public ResponseEntity<?> isClubHasAnyUsers(@PathVariable int id)
    {
        var data = clubService.isHasAnyUsersInClubById(id);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/places/{id}")
    public ResponseEntity<?> patchPlaceData(@PathVariable int id, @RequestBody Map<String, Object> updates)
    {
        try {
            placeService.patch(id, updates);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @GetMapping("/places")
    public ResponseEntity<?> getPlaces(@RequestParam("page") Integer page,
                                       @RequestParam("count") Integer count)
    {
        var data = placeService.getAll(PageRequest.of(page, count));
        return ResponseEntity.ok(data);
    }

    @GetMapping("/places/{id}")
    public ResponseEntity<?> getPlaceById(@PathVariable int id)
    {
        var data = placeService.getByID(id);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/places/{id}/hasAnyGames")
    public ResponseEntity<?> isPlaceHasAnyGames(@PathVariable int id)
    {
        var data = placeService.entityHasAnyGamesById(id);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/places/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id)
    {
        try {
            placeService.delete(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }


    @DeleteMapping("/clubs/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable int id)
    {
        try {
            clubService.Delete(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @PostMapping("/clubs")
    public ResponseEntity<?> addClub( @RequestBody Map<String, Object> data)
    {

        try {
            var newClub = new Club();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "clubName" -> newClub.setClubName((String)value);
                    case "reportGroup" -> newClub.setReportGroup((String)value);
                    case "defaultClub" -> newClub.setIsDefaultClub((Boolean)value);
                    case "city" -> {
                        var city = cityService.getByID((Integer) value);
                        city.ifPresent(newClub::setCity);
                    }
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }




            return ResponseEntity.ok(clubService.AddHero(newClub));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }





    @PostMapping("/places")
    public ResponseEntity<?> addPlace( @RequestBody Map<String, Object> data)
    {

        try {
            var newPlace = new Place();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "description" -> newPlace.setDescription((String)value);
                    case "address" -> newPlace.setAddress((String)value);
                    case "city" -> {
                        var city = cityService.getByID((Integer) value);
                        city.ifPresent(newPlace::setCity);
                    }
                    case "isArchived" -> newPlace.setIsArchived((Boolean) value);
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            placeService.add(newPlace);



            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }



    @GetMapping("/cities")
    public ResponseEntity<?> getCities()
    {
        var data = cityService.getAll();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEvents()
    {
        var data = eventService.getAll();
        return ResponseEntity.ok(data);
    }


    @GetMapping("/events/{id}")
    public ResponseEntity<?> getUserByEvent(@PathVariable(name="id") Integer id)
    {
        var data = eventService.getEventByIdThroughtProjection(id, EventWithUserUsernameList.class);
        return ResponseEntity.ok(data);
    }


    @GetMapping("/events/{id}/isLoggedUserRegistered")
    public ResponseEntity<?> isLoggedUserRegisteredOnEvent(@PathVariable(name="id") Integer id)
    {
        var result = userInEventService.getById(new UserInEventPK(authService.GetAuthenticatedUserData().getId(), id));
        if (result.isPresent()) {
            return ResponseEntity.ok(true);
        }
        else
        {
            return ResponseEntity.ok(false);
        }
    }


    @GetMapping("/security/simple_principal")
    public ResponseEntity<?> getSimpleData()
    {
        var result = userAdditionalService.getByProjectionById(authService.GetAuthenticatedUserData().getId(), UsernameUserProjection.class);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/events/{id}/userStatistics")
    public ResponseEntity<?> getUserStatistics(@PathVariable(name="id") Integer id)
    {
        var result = statisticsOfUserService.getAllStatisticsOfUsersByEventId(id);
        return ResponseEntity.ok(result);
    }


    @PatchMapping("/usersInEvents/{userId}/{eventId}")
    public ResponseEntity<?> patchUserInEvent(@PathVariable(name="userId") Integer userId, @PathVariable(name="eventId") Integer eventId, @RequestBody Map<String, Object> updates)
    {
        try {
            userInEventService.patch(userId, eventId, updates);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }


    @PatchMapping("/events/{eventId}")
    public ResponseEntity<?> patchEvent( @PathVariable(name="eventId") Integer eventId, @RequestBody Map<String, Object> updates)
    {
        try {
            eventService.patch(eventId, updates);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent( @PathVariable(name="eventId") Integer eventId)
    {
        try {
            eventService.deleteById(eventId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }
    @PostMapping("usersInEvents/{userId}/{eventId}")
    public ResponseEntity<?> postUserInEvent(@PathVariable(name="userId") Integer userId, @PathVariable(name="eventId") Integer eventId, @RequestBody Map<String, Object> updates )
    {
        try {
            userInEventService.save(userId, eventId, updates);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @PostMapping("/events")
    public ResponseEntity<?> postEvent(@RequestBody Map<String, Object> updates )
    {
        try {
            var result = eventService.save(updates);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @DeleteMapping("/usersInEvents/{userId}/{eventId}")
    public ResponseEntity<?> deleteUserInEvent(@PathVariable(name="userId") Integer userId, @PathVariable(name="eventId") Integer eventId)
    {
        try {
            userInEventService.deleteById(userId, eventId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user update: "+ e.getMessage());
        }
    }

    @GetMapping("/distribute/{id}")
    public ResponseEntity<?> distribute(@PathVariable("id") Integer id)
    {
        var data = analiticsService.breakUsersByTables(id, UsernameUserWithRatingProjection.class);
        return ResponseEntity.ok(data);
    }


    @GetMapping("/users/{id}/getGamesCountForLatest{count}Days")
    public ResponseEntity<?> getLastdays(@PathVariable("id") Integer id, @PathVariable("count") Integer count)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -count);
        java.util.Date startDate =  calendar.getTime();
        var data = analiticsService.countGamesPlayedSinceDateByUserById(id, startDate );
        return ResponseEntity.ok(data);
    }

    @GetMapping("/users/leads")
    public ResponseEntity<?> getAllLeads()
    {

        var data = userAdditionalService.getAllByRoleId(3, UsernameUserProjection.class);
        return ResponseEntity.ok(data);
    }



}
