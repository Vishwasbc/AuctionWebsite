package com.auctionservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.auctionservice.entity.Auction;
import com.auctionservice.entity.AuctionStatus;
import com.auctionservice.repository.AuctionRepository;

import lombok.AllArgsConstructor;

/**
 * Service implementation class for scheduling auction-related tasks.
 */
@Service
@AllArgsConstructor
public class AuctionSchedulerImpl implements AuctionScheduler {
    private AuctionRepository auctionRepository;

    /**
     * Starts auction that are scheduled to start.
     * This method runs every 10 seconds.
     */
    @Override
    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void startAuctions() {
        List<Auction> upcomingAuctions = auctionRepository.findByStatus(AuctionStatus.UPCOMING);
        LocalDateTime now = LocalDateTime.now();
        for (Auction auction : upcomingAuctions) {
            if (auction.getStartDate().isBefore(now)) {
                auction.setStatus(AuctionStatus.LIVE);
                auctionRepository.save(auction);
            }
        }
    }

    /**
     * Ends auction that are scheduled to end.
     * This method runs every 10 seconds.
     */
    @Override
    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void endAuctions() {
        List<Auction> ongoingAuctions = auctionRepository.findByStatus(AuctionStatus.LIVE);
        LocalDateTime now = LocalDateTime.now();
        for (Auction auction : ongoingAuctions) {
            if (auction.getEndDate().isBefore(now)) {
                auction.setStatus(AuctionStatus.ENDED);
                auctionRepository.save(auction);
                System.out.println("Auction " + auction.getAuctionId() + " ended at " + now);
            }
        }
    }
}
