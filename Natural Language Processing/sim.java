

public class sim{

	private static SentimentAnalyzer analyzer = null;

	private static String message = "Use like:$ java sim METHOD INPUT_FILE\n*************************************";


	public static void main(String[] args) {

		if(args.length == 2){
    		analyzer = new SentimentAnalyzer(args[1]);
		}
		
		try{
			if(args[0].equals("baseline")){
    			analyzer.baseline();
			}else if(args[0].equals("tf-idf")){
				analyzer.tfidf();
			}else if(args[0].equals("wordnet")){
				analyzer.wordnet();
			}else if(args[0].equals("word2vec")){
				analyzer.word2vec();
			}else if(args[0].equals("custom")){
				analyzer.custom();
			}else{ System.out.println(message); }
    	}catch(ArrayIndexOutOfBoundsException e){
    		e.printStackTrace();
    	}
	}

}