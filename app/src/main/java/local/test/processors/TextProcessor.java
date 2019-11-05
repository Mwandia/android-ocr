package local.test.processors;

public class TextProcessor {
    private String[] details;

    public TextProcessor(String details){
        this.details = details.split("\n");
    }

    public int getID(){
        int id = 0;

        for(int i = 0; i<details.length; i++){
            String[] line = details[i].split(" ");
            for(int j = 0; j<line.length; j++) {
                String word = line[j];
                if (word.equals("NUMBER") && line[j+1].length() == 8) {
                    id = Integer.parseInt(line[j+1]);
                    break;
                }
            }
        }


        return id;
    }

    public String getBirthdate(){
        String date = "";

        for(int i = 0; i<details.length; i++) {
            String line = details[i];
            if (line.equals("DATE OF BIRTH")) {
                date = details[i + 1];
                break;
            }
        }

        return date;
    }

    public String getNames(){
        String names = "";

        for(int i = 0; i<details.length; i++){
            String line = details[i];
            if(line.equals("FULL NAMES")){
                names = details[i+1];
                break;
            }
        }

        return names;
    }

    public String getSex(){
        String sex = "";

        for(int i = 0; i<details.length; i++){
            String line = details[i];
            if(line.equals("SEX")){
                sex = details[i+1];
                break;
            }
        }

        return sex;
    }


}
