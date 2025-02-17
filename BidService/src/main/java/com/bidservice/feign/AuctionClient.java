package com.bidservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bidservice.dto.AuctionDto;
 
@FeignClient("AUCTIONSERVICE")
public interface AuctionClient {
    @GetMapping("/api/auction/{auctionId}")
    AuctionDto getAuctionById(@PathVariable Integer auctionId);
    @PostMapping("/api/auction/{id}/{price}")
    void updateHighestBid(@PathVariable int id,@PathVariable double price);
}