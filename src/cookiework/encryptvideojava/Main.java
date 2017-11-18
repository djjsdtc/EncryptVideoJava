package cookiework.encryptvideojava;

import ffmpegenc.Encryptor;
import ffmpegenc.ICallback;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    private static class MyCallback implements ICallback{
        private DBProcessor dbProcessor;
        private int id;

        public MyCallback(DBProcessor dbProcessor, int id) {
            this.dbProcessor = dbProcessor;
            this.id = id;
        }

        @Override
        public void onSuccess(Properties unused) {
            try {
                dbProcessor.execute(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Properties errorInfo) {

        }
    }

    public static void main(String[] args) throws SQLException {
        CommandLineParser parser = new DefaultParser();
        Options options = buildOptions();
        try {
            CommandLine cmd = parser.parse(options, args);
            int id = Integer.parseInt(cmd.getOptionValue('n'));
            if(id <= 0) throw new NumberFormatException();
            Properties prop = new Properties();
            FileReader reader = new FileReader(cmd.getOptionValue('p'));
            prop.load(reader);
            checkProperty(prop);
            DBProcessor processor = new DBProcessor(
                    prop.getProperty("database_address"),
                    prop.getProperty("database_username"),
                    prop.getProperty("database_password")
            );
            Encryptor encryptor = new Encryptor(prop.getProperty("ffmpeg_path", null), new MyCallback(processor, id));
            encryptor.runEncrypt(createFfmpegProperties(
                    cmd.getOptionValue('i'),
                    cmd.getOptionValue('o')
            ));
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java cookiework.encryptvideojava.Main", options, true);
        } catch (FileNotFoundException e) {
            System.out.println("Property file not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid video id.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private static Options buildOptions(){
        Options options = new Options();
        options.addOption(Option.builder("i").longOpt("input").hasArg().argName("INPUT").required().desc("input file").build());
        options.addOption(Option.builder("o").longOpt("output").hasArg().argName("OUTPUT").required().desc("output directory").build());
        options.addOption(Option.builder("n").longOpt("id").hasArg().argName("ID").required().desc("video id in database").build());
        options.addOption(Option.builder("p").longOpt("prop").hasArg().argName("PROP").required().desc("property file").build());
        return options;
    }

    private static void checkProperty(Properties prop) throws IllegalArgumentException {
        if(!prop.containsKey("database_address") ||
                !prop.containsKey("database_username") ||
                !prop.containsKey("database_password")){
            throw new IllegalArgumentException("properties file format error");
        }
    }

    private static Properties createFfmpegProperties(String input, String output){
        Properties properties = new Properties();
        properties.setProperty("input", input);
        properties.setProperty("output", output);
        return properties;
    }
}
