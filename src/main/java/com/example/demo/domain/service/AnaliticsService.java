package com.example.demo.domain.service;

import AnaliticsService.domain.StrategyTypes.DistributionType;
import AnaliticsService.domain.StrategyTypes.SortType;
import com.example.demo.domain.model.Game;
import com.example.demo.domain.model.GameMember;
import com.example.demo.domain.model.StatisticsOfUser;
import com.example.demo.domain.repository.GameMemberRepository;
import com.example.demo.domain.repository.GameRepository;
import com.example.demo.domain.service.DistributionUsersStrategies.DistributeStrategyFactory;
import com.example.demo.domain.service.SortUsersStrategies.SortStrategyFactory;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserAdditionalProjection;
import com.example.demo.domain.repository.StatisticsOfUserRepository;
import com.example.demo.domain.repository.UserAdditionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AnaliticsService {

    @Autowired
    private final UserAdditionalRepository userAdditionalRepository;

    @Autowired
    private final StatisticsOfUserRepository statisticsOfUserRepository;

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final GameMemberRepository gameMemberRepository;

    private final SortStrategyFactory sortStrategyFactory;
    private final DistributeStrategyFactory distributeStrategyFactory;
    public AnaliticsService(UserAdditionalRepository userAdditionalRepository, StatisticsOfUserRepository statisticsOfUserRepository, GameRepository gameRepository, GameMemberRepository gameMemberRepository, SortStrategyFactory sortStrategyFactory, DistributeStrategyFactory distributeStrategyFactory) {
        this.userAdditionalRepository = userAdditionalRepository;
        this.statisticsOfUserRepository = statisticsOfUserRepository;
        this.gameRepository = gameRepository;
        this.gameMemberRepository = gameMemberRepository;

        this.sortStrategyFactory = sortStrategyFactory;
        this.distributeStrategyFactory = distributeStrategyFactory;
    }


    public <T extends UserAdditionalProjection> List<List<T>> breakUsersByTables(Integer eventId, Class<T> projectionType)
    {
        var statistics = statisticsOfUserRepository.findStatisticsByEventId(eventId);
        var sorted_statistics = sortStrategyFactory.execute(SortType.BASIC, statistics);
        var distribution = distributeStrategyFactory.execute(DistributionType.BASIC, sorted_statistics);
        var resultList = new ArrayList<List<T>>();
        for (var list:distribution) {
            var sublist = new ArrayList<T>();
            for (var userStatistics:list) {
                var user = userAdditionalRepository.getUserByProjectionById(userStatistics.getId(), projectionType);
                user.ifPresent(sublist::add);

            }
            resultList.add(sublist);
        }
        return resultList;
    }
    //Gfhkldsfklshjflksdjflksdfj
    public int countGamesPlayedSinceDateByUserById(int userId, Date sinceDate)
    {
        return gameMemberRepository.countGamesPlayedSinceDate(userId, sinceDate);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void computeRatingOfUsersByGame(Game game)
    {
            var members = game.getMembers();
            var averageRating = computeAverageRating(members);
            for (var member : members) {
                var userStatistics = statisticsOfUserRepository.findById(member.getId().getUserId());
                if (userStatistics.isEmpty()) {
                    break;
                }

                userStatistics.get().setExtraPoints(userStatistics.get().getExtraPoints() + member.getExtraPoints());
                userStatistics.get().setCompensationPoints(userStatistics.get().getCompensationPoints() + member.getBM_Compensation());
                userStatistics.get().setPenaltyPoints(userStatistics.get().getPenaltyPoints() + member.getPenalty());

                switch (member.getRole().getId()) {
                    case 1 -> userStatistics.get().setGamesAsCitizen(userStatistics.get().getGamesAsCitizen() + 1);
                    case 2 -> userStatistics.get().setGamesAsMafia(userStatistics.get().getGamesAsMafia() + 1);
                    case 10 -> userStatistics.get().setGamesAsSheriff(userStatistics.get().getGamesAsSheriff() + 1);
                    case 11 -> userStatistics.get().setGamesAsDon(userStatistics.get().getGamesAsDon() + 1);
                }

                userStatistics.get().setGamesCount(userStatistics.get().getGamesCount() + 1);
                userStatistics.get().setTwoBlackCount(userStatistics.get().getTwoBlackCount() + member.getBM_2Black());
                userStatistics.get().setThreeBlackCount(userStatistics.get().getThreeBlackCount() + member.getBM_3Black());
                userStatistics.get().setThreeRedCount(userStatistics.get().getThreeRedCount() + member.getBM_3Red());


                if (member.isWin()) {
                    computeRating(averageRating, userStatistics.get());
                    switch (member.getRole().getId()) {
                        case 1 -> userStatistics.get().setWinsAsCitizen(userStatistics.get().getWinsAsCitizen() + 1);
                        case 2 -> userStatistics.get().setWinsAsMafia(userStatistics.get().getWinsAsMafia() + 1);
                        case 10 -> userStatistics.get().setWinsAsSheriff(userStatistics.get().getWinsAsSheriff() + 1);
                        case 11 -> userStatistics.get().setWinsAsDon(userStatistics.get().getWinsAsDon() + 1);
                    }
                }

                userStatistics.get().setAveragePointsByGame(userStatistics.get().getAveragePointsByGame() / userStatistics.get().getRating());
                statisticsOfUserRepository.save(userStatistics.get());
            }

    }

    private void computeRating(Float averageRatingByGame, StatisticsOfUser statistics)
    {
        var kCoeff = 2;
        var eCoeff = 1 / (1 + Math.pow(10, (averageRatingByGame - statistics.getRating()/50f)));
        var result = ((float)(kCoeff * (1 - eCoeff)));
        statistics.setRating(statistics.getRating() + result);
    }


    private Float computeAverageRating(List<GameMember> members)
    {
        var averageRating = 0f;
        for (var member:members) {
            var userStatistics = statisticsOfUserRepository.findById(member.getId().getUserId());
            if (userStatistics.isEmpty()) {
                break;
            }
            averageRating += userStatistics.get().getRating();
        }
        return averageRating/=members.size();
    }

}
