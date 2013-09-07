package com.mahout.reccomendation;

import org.apache.mahout.cf.taste.common.TasteException;

/**
 * Entry class of Song Recommender
 * 
 * @author kailash Joshi
 * 
 */
public class RunMe {
	/**
	 * Runs Song Recommender System
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RecommendSongList songList = new RecommendSongList();
		String userName = "Fernando Arguelles";

		try {
			System.out.println("Recommended Songs for " + userName + " \n\n");
			for (String result : songList.getSimilarSongList(userName)) {
				System.out.println(result);
			}
		} catch (TasteException e) {
			e.printStackTrace();
		}
	}
}
