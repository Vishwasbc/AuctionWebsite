package com.gateway.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PathData {
	private String userPath;
	private String productPath;
	private  String auctionPath;
	private String bidPath;
}