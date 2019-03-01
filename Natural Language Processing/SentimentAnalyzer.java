import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.NoSuchElementException; 
import java.util.ArrayList;	
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Enumeration;
import java.util.List;
import java.lang.Math;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;
import edu.mit.jwi.data.parse.DataLineParser;

public class SentimentAnalyzer{

	private String file = null;
	private String outputFile = null;
	private Double[] output = null;
	private Double[] output2 = null;
	private int lengthOfVec;

	private StringTokenizer tokenizer = null;
	private FileReader fileReader = null;
	private BufferedReader bufferedReader = null;
	private PrintWriter writer = null;
	private PrintWriter writer2 = null;

	private ArrayList<String> inputList = null;
	private ArrayList<String> vecList = null;
	private ArrayList<String> idfList = null;
	private ArrayList<String> stopList = null;
	private Hashtable<String,Double> wordVecModel = null;
	private Hashtable<String,Double[]> vecHash = null;
	private Hashtable<String,Double> idfHash = null;
	private Hashtable<String,Integer> stopHash = null;
	private Double idfMaxValue = 0.0;
	
	private IDictionary dict = null;

	public SentimentAnalyzer(String file){
		this.file = file;
		String[] h = file.split("([\\.])");
		outputFile = "STS.output." + h[2] + ".txt";
		inputList = new ArrayList<String>();

		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			readFile(1);

			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readFile(int in){
		try {
			if(in == 1){ while(bufferedReader.ready()){ inputList.add(bufferedReader.readLine()); } } 
			else if(in == 2){ while(bufferedReader.ready()){ vecList.add(bufferedReader.readLine()); } } 
			else if(in == 3){ while(bufferedReader.ready()){ idfList.add(bufferedReader.readLine()); } }
			else if(in == 4){ while(bufferedReader.ready()){ stopList.add(bufferedReader.readLine()); } }

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printFile(){
		try{
    		writer = new PrintWriter(outputFile, "UTF-8");
			for(int i = 0; i<output.length; i++){
				writer.println(output[i]);
			}
			writer.close();
			System.out.println("-A File named '"+ outputFile +"' has been created.");
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}		
	}

	private void computeVecTxt(){
		try{
			vecList = new ArrayList<String>();
			fileReader = new FileReader("vec.txt");
			bufferedReader = new BufferedReader(fileReader);
			readFile(2);
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		vecHash = new Hashtable<String,Double[]>();
		String bloo = vecList.get(0).split("([^\\w+])")[0];
		lengthOfVec = Integer.parseInt(vecList.get(0).split("([^\\w+])")[1]);
		for(int o = 1; o < Integer.parseInt(bloo); o++){
			String[] tempS = vecList.get(o).split("( )");
			String[] tempArr = vecList.get(o).split("( )");
			Double[] tempArrD = new Double[lengthOfVec];
			for(int i = 1; i<101; i++){
				tempArrD[i-1] = (Double)Double.parseDouble(tempArr[i]);
			}
			if(vecHash.get(tempS[0]) == null){
				vecHash.put(tempS[0],tempArrD);
			}
		}
	}

	private void createWordVec(){
		wordVecModel = new Hashtable<String,Double>();
		for(String x : inputList){
			String[] d = x.split("([^\\w+])");
			for(String y : d){
				if(wordVecModel.get(y) == null){
					wordVecModel.put(y,0.0);
				}
			}
		}
	}

	private void computeIdfTxt(){
		try{
			idfList = new ArrayList<String>();
			fileReader = new FileReader("idf.txt");
			bufferedReader = new BufferedReader(fileReader);
			readFile(3);
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		idfHash = new Hashtable<String,Double>();
		for(String o : idfList){
			String[] tempS = o.split("( )");
			if(idfHash.get(tempS[0]) == null){
				idfHash.put(tempS[0],Double.parseDouble(tempS[1]));
			}
		}
		Enumeration<Double> en = idfHash.elements();
		while(en.hasMoreElements()){
			Double temp = en.nextElement();
			if(temp > idfMaxValue){
				idfMaxValue = temp;
			}
		}
	}
	private String[] tokenizeMe(String in){
			String[] inputString = in.split("([^\\w+])");

			for(String x : inputString) x = x.toLowerCase();

			return inputString;
	}

	private void computeStopWords(){
		try{
			stopList = new ArrayList<String>();
			fileReader = new FileReader("stopwords.txt");
			bufferedReader = new BufferedReader(fileReader);
			readFile(4);
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		stopHash = new Hashtable<String,Integer>();
		for(String o : stopList){
			stopHash.put(o,0);
		}
	}

	public void baseline(){
		createWordVec();
		output = new Double[inputList.size()];

		for(int z = 0; z < inputList.size(); z++){
			String[] d = inputList.get(z).split("(	)");
			String[] split1 = tokenizeMe(d[0]);
			String[] split2 = tokenizeMe(d[1]);
			Hashtable<String,Double> vector1 = new Hashtable<String,Double>();
			vector1.putAll(wordVecModel);
			Hashtable<String,Double> vector2 = new Hashtable<String,Double>();
			vector2.putAll(wordVecModel);

			for(String w : split1){
				if(vector1.containsKey(w)){
					vector1.put(w, 1.0);
				}
			}
			for(String w : split2){
				if(vector2.containsKey(w)){
					vector2.put(w, 1.0);
				}
			}
			Enumeration<Double> vn1 = vector1.elements();
			Enumeration<Double> vn2 = vector2.elements();
			
			Double value = 0.0;
			Double value1Sq = 0.0;
			Double value2Sq = 0.0;
			while(vn1.hasMoreElements()){
				Double tempValue = vn1.nextElement();
				Double tempValue2 = vn2.nextElement();
				value= value + tempValue * tempValue2;
				value1Sq = value1Sq + tempValue * tempValue;
				value2Sq = value2Sq + tempValue2 * tempValue2;
			}
			value1Sq = Math.sqrt(value1Sq);
			value2Sq = Math.sqrt(value2Sq);
			
			//value1Sq = Math.log(value1Sq);
			//value2Sq = Math.log(value2Sq);

			output[z] = 5*(value / (value1Sq * value2Sq));
		}
		printFile();
	}

	public void tfidf(){
		createWordVec();
		computeIdfTxt();
		output = new Double[inputList.size()];

		for(int z = 0; z < inputList.size(); z++){
			String[] d = inputList.get(z).split("(	)");
			String[] split1 = tokenizeMe(d[0]);
			String[] split2 = tokenizeMe(d[1]);
			Hashtable<String,Double> vector1 = new Hashtable<String,Double>();
			vector1.putAll(wordVecModel);
			Hashtable<String,Double> vector2 = new Hashtable<String,Double>();
			vector2.putAll(wordVecModel);

			for(String w : split1){
				if(vector1.containsKey(w)){
					if(idfHash.containsKey(w)){ vector1.put(w, idfHash.get(w));
					}
					else{
						vector1.put(w, idfMaxValue);
					}
				}
			}
			for(String w : split2){
				if(vector2.containsKey(w)){
					if(idfHash.containsKey(w)){ vector2.put(w, idfHash.get(w));
					}
					else{
						vector2.put(w, idfMaxValue);
					}
				}
			}
			Enumeration<Double> vn1 = vector1.elements();
			Enumeration<Double> vn2 = vector2.elements();
			
			Double value = 0.0;
			Double value1Sq = 0.0;
			Double value2Sq = 0.0;
			while(vn1.hasMoreElements()){
				Double tempValue = vn1.nextElement();
				Double tempValue2 = vn2.nextElement();
				value= value + tempValue * tempValue2;
				value1Sq = value1Sq + tempValue * tempValue;
				value2Sq = value2Sq + tempValue2 * tempValue2;
			}
			value1Sq = Math.sqrt(value1Sq);
			value2Sq = Math.sqrt(value2Sq);
			
			//value1Sq = Math.log(value1Sq);
			//value2Sq = Math.log(value2Sq);

			output[z] = 5*(value / (value1Sq * value2Sq));
		}
		printFile();
	}

	public void wordnet(){
		/*try{
			dict = new Dictionary(new File("dict"));
			dict.open();
			DataLineParser g = DataLineParser.getInstance();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}*/
 
		output = new Double[inputList.size()];

		for(int z = 0; z < inputList.size(); z++){
			String[] d = inputList.get(z).split("(	)");
			String[] split1 = tokenizeMe(d[0]);
			String[] split2 = tokenizeMe(d[1]);		

			Hashtable<String,String[]> vector1 = new Hashtable<String,String[]>();
			Hashtable<String,String[]> vector2 = new Hashtable<String,String[]>();

			/*
			for(String w : split1){
				if(vector1.containsKey(w)){
					vector1.put(w, 1.0);
				}
			}
			for(String w : split2){
				if(vector2.containsKey(w)){
					vector2.put(w, 1.0);
				}
			}
			/*
			Enumeration<Double> vn1 = vector1.elements();
			Enumeration<Double> vn2 = vector2.elements();
			
			Double value = 0;
			Double value1Sq = 0;
			Double value2Sq = 0;
			while(vn1.hasMoreElements()){
				Double tempValue = vn1.nextElement();
				Double tempValue2 = vn2.nextElement();
				value= value + tempValue * tempValue2;
				value1Sq = value1Sq + tempValue * tempValue;
				value2Sq = value2Sq + tempValue2 * tempValue2;
			}
			value1Sq = Math.sqrt(value1Sq);
			value2Sq = Math.sqrt(value2Sq);
			
			value1Sq = Math.log(value1Sq);
			value2Sq = Math.log(value2Sq);

			output[z] = (value / (value1Sq * value2Sq));
		*/}
		//printFile();
	}

	public void word2vec(){
		computeVecTxt();
		output = new Double[inputList.size()];

		for(int z = 0; z < inputList.size(); z++){
			String[] d = inputList.get(z).split("(	)");
			String[] split1 = tokenizeMe(d[0]);
			String[] split2 = tokenizeMe(d[1]);
			Hashtable<String,Double[]> vectorOfWordVectors1 = new Hashtable<String,Double[]>();
			Hashtable<String,Double[]> vectorOfWordVectors2 = new Hashtable<String,Double[]>();
			
			Double[] sumOfVecs1 = new Double[lengthOfVec];
			Double[] sumOfVecs2 = new Double[lengthOfVec];
			for(int k = 0; k < sumOfVecs1.length; k++) sumOfVecs1[k] = 0.0;
			for(int k = 0; k < sumOfVecs2.length; k++) sumOfVecs2[k] = 0.0;

			for(String w : split1){
				if(vecHash.containsKey(w)){
					vectorOfWordVectors1.put(w, vecHash.get(w));
				}/*else{
					vectorOfWordVectors1.put(w, sumOfVecs1);
				}*/
			}
			for(String w : split2){
				if(vecHash.containsKey(w)){
					vectorOfWordVectors2.put(w, vecHash.get(w));
				}/*else{
					vectorOfWordVectors2.put(w, sumOfVecs1);
				}*/
			}
			Enumeration<Double[]> listOfWords1 = vectorOfWordVectors1.elements();
			Enumeration<Double[]> listOfWords2 = vectorOfWordVectors2.elements();

			while(listOfWords1.hasMoreElements()){
				Double[] tempVec = listOfWords1.nextElement();
				for(int k = 0; k<lengthOfVec; k++){
					sumOfVecs1[k] = sumOfVecs1[k] + tempVec[k];
				}
			}
			while(listOfWords2.hasMoreElements()){
				Double[] tempVec = listOfWords2.nextElement();
				for(int k = 0; k<sumOfVecs2.length; k++){
					sumOfVecs2[k] = sumOfVecs2[k] + tempVec[k];
				}
			}

			Double sumTotal = 0.0;
			Double sumDenom1 = 0.0;
			Double sumDenom2 = 0.0;

			for(int k = 0; k<sumOfVecs1.length; k++){
				sumTotal = sumTotal + sumOfVecs1[k]*sumOfVecs2[k];
				sumDenom1 = sumDenom1 + sumOfVecs1[k]*sumOfVecs1[k];
				sumDenom2 = sumDenom2 + sumOfVecs2[k]*sumOfVecs2[k];
			}

			sumDenom1 = Math.sqrt(sumDenom1);
			sumDenom2 = Math.sqrt(sumDenom2);

			//sumTotal = Math.log(sumTotal);	
			sumTotal = Math.max(0.0, sumTotal);

			sumDenom1 = Math.log(sumDenom1);
			sumDenom2 = Math.log(sumDenom2);
			Double denominator = sumDenom1 * sumDenom2;

			output[z] = (sumTotal / denominator);
		}

		printFile();

	}

	public void custom(){
		createWordVec();
		computeStopWords();
		output = new Double[inputList.size()];

		for(int z = 0; z < inputList.size(); z++){
			String[] d = inputList.get(z).split("(	)");
			String[] split1 = tokenizeMe(d[0]);
			String[] split2 = tokenizeMe(d[1]);
			Hashtable<String,Double> vector1 = new Hashtable<String,Double>();
			vector1.putAll(wordVecModel);
			Hashtable<String,Double> vector2 = new Hashtable<String,Double>();
			vector2.putAll(wordVecModel);

			for(String w : split1){
				if(vector1.containsKey(w) && !stopHash.containsKey(w)){
					vector1.put(w, 1.0);
				}
			}
			for(String w : split2){
				if(vector2.containsKey(w) && !stopHash.containsKey(w)){
					vector2.put(w, 1.0);
				}
			}
			Enumeration<Double> vn1 = vector1.elements();
			Enumeration<Double> vn2 = vector2.elements();
			
			Double value = 0.0;
			Double value1Sq = 0.0;
			Double value2Sq = 0.0;
			while(vn1.hasMoreElements()){
				Double tempValue = vn1.nextElement();
				Double tempValue2 = vn2.nextElement();
				value= value + tempValue * tempValue2;
				value1Sq = value1Sq + tempValue * tempValue;
				value2Sq = value2Sq + tempValue2 * tempValue2;
			}
			value1Sq = Math.sqrt(value1Sq);
			value2Sq = Math.sqrt(value2Sq);
			
			//value1Sq = Math.log(value1Sq);
			//value2Sq = Math.log(value2Sq);

			output[z] = (value / (value1Sq * value2Sq));
		}
		printFile();
	}
}