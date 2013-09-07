package com.mahout.reccomendation;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVParser;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;

/**
 * Model Class for recommending Songs using Mahout Recommender System
 * 
 * @author Kailash Joshi
 * 
 */
public class Model {
	// Local feild
	private Recommender recommender = null;
	private MemoryIDMigrator strTolng = new MemoryIDMigrator();
	private DataModel model;

	/**
	 * Getter for converting string to Long value
	 * 
	 * @return MemoryIDMigrator
	 */
	public MemoryIDMigrator getStrTolng() {
		return strTolng;
	}

	/**
	 * This method creates model for Song Recommendation
	 * 
	 * @return recommender Object
	 */
	public Recommender songModel() {
		String[] line;

		try {
			// Preference is a interface which represent one Preference per user
			Map<Long, List<Preference>> userPrefLists = new HashMap<Long, List<Preference>>();
			CSVParser csv = new CSVParser(new InputStreamReader(
					new FileInputStream(
							"src/com/mahout/reccomendation/Dataset.csv"),
					"UTF-8"));
			csv.getLine();
			List<Preference> userPrefrence;
			while ((line = csv.getLine()) != null) {
				String user = line[0];
				String song = line[1];

				long userLong = strTolng.toLongID(user);
				strTolng.storeMapping(userLong, user);

				long songLong = strTolng.toLongID(song);
				strTolng.storeMapping(songLong, song);

				if ((userPrefrence = userPrefLists.get(userLong)) == null) {
					userPrefrence = new ArrayList<Preference>();
					userPrefLists.put(userLong, userPrefrence);
				}
				userPrefrence.add(new GenericPreference(userLong, songLong, 1));
			}

			// FastByIDMap is like HashMap which uses less memory. FastByIDMap
			// is Mahout HashMap
			FastByIDMap<PreferenceArray> prefrenceHashMap = new FastByIDMap<PreferenceArray>();

			for (Entry<Long, List<Preference>> entry : userPrefLists.entrySet()) {
				prefrenceHashMap.put(entry.getKey(),
						new GenericUserPreferenceArray(entry.getValue()));
			}

			model = new GenericDataModel(prefrenceHashMap);
			recommender = new GenericBooleanPrefItemBasedRecommender(model,
					new LogLikelihoodSimilarity(model));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return recommender;
	}
}
