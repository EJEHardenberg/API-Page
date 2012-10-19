package danger_zone;
import java.util.HashMap;
import java.io.*;

/**
*@author Ethan Eldridge <ejayeldridge @ gmail.com>
*@version 0.0
*@since 2012-10-17
*
* Naive Bayes classifier to classify a tweet as a danger or not. 
*/
public class NaiveBayes{
	/**
	*Constant for the category danger
	*/
	public static final int CAT_DANGER = 0;
	/**
	*Constant for the category safe
	*/
	public static final int CAT_SAFE = 1;
	/**
	*Lemmatizer instance to strip and parse tweets into a managable form.
	*/
	private Lemmatizer tweetStripper = new Lemmatizer();
	/**
	*Helper array to make the classifying function easier
	*/
	private static final int [] categories = {CAT_SAFE,CAT_DANGER};

	/**
	*Holds The two categories of the data being classified
	*/
	private HashMap<Integer, HashMap<String,Integer>> category_count = new HashMap<Integer, HashMap<String,Integer>>();

	public NaiveBayes(){
		HashMap<String,Integer> danger = new HashMap<String,Integer>();
		HashMap<String,Integer> safe = new HashMap<String,Integer>();
		category_count.put(CAT_DANGER, danger);
		category_count.put(CAT_SAFE, safe);
	}

	/**
	*Trains the algorithm on the dataset, each category is specified by a constant declared in this class, CAT_SAFE, CAT_DANGER and such.
	*/
	public void train(int category, String tweet){


		//Get the words out of the tweet
		String [] parsedTweet = tweetStripper.parseTweet(tweet).split(" ");

		//Associate these words with the category
		for(String pt : parsedTweet){
			if(!category_count.get(category).containsKey(pt)){
				category_count.get(category).put(pt,1);
			}
			int numW = category_count.get(category).get(pt);
			category_count.get(category).put(pt,numW+1);

		}
		//count of pt in cat divided by total count of pt in all categories = probability
	}

	public int classify(String tweet){
		//Each word is classified indenpendtly, whichever one has the most wins. 
		String [] parsedTweet = tweetStripper.parseTweet(tweet).split(" ");

		//Classify each word
		for(String pt : parsedTweet){
			for(int cat : categories){
				if(category_count.get(cat).containsKey(pt)){

				}else{

				}
			}
		}

	}

	public static void main(String[] args) {
		NaiveBayes nb = new NaiveBayes();

		nb.train(NaiveBayes.CAT_SAFE,"I love to dance with Kittens");
		nb.train(NaiveBayes.CAT_DANGER,"There are Kittens on Fire and its terrible those poor Kittens");



	}

}