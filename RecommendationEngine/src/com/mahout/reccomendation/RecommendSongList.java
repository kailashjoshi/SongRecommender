package com.mahout.reccomendation;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
/**
 * Takes recommender object from Model class and convert each song into string
 * And return an array of Song List
 * @author kailash Joshi
 *
 */
public class RecommendSongList {

	private Model model = new Model();
	
	/**
	 * This method take all the song list from recommender object created in Model class
	 * And convert long value into string
	 * @param userName
	 * @return array of song list
	 * @throws TasteException
	 */
	public String[] getSimilarSongList(String userName) throws TasteException {
		List<String> songList = new ArrayList<String>();
		try {
			List<RecommendedItem> songs = model.songModel().recommend(model.getStrTolng().toLongID(userName), 10);
			for(RecommendedItem song : songs) {
				songList.add(model.getStrTolng().toStringID(song.getItemID()));
			}
		} catch (TasteException e) {
			throw e;
		}
		return songList.toArray(new String[songList.size()]);
	}

}
