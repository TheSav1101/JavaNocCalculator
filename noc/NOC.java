package noc;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NOC{

    

    //Usage: java NOC <path_to_project> $(find . -name '*.java' -print)
    public static void main(String[] args){
        Path start = Paths.get(args[0]);
        List<String> collect = null;
        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
                collect = stream
                .map(String::valueOf)
                .sorted()
                .collect(Collectors.toList());
            
            //collect.forEach(System.out::println);
        }catch(Exception e){
            System.out.println("Something bad happened...");
        }
        
        ClassTree t = new ClassTree();
        File f = null;
        Scanner sc = null;
        Iterator<String> lit = collect.iterator();
        String fs = null;
        while(lit.hasNext()){
            try{
                fs = lit.next();
                boolean inComment = false;
                boolean willBeComment = false;
                if(fs.substring(fs.length() - 5,fs.length()).equals(".java")){
                    f = new File(fs);
                    sc = new Scanner(f);
                    while(sc.hasNextLine()){
                        String line = sc.nextLine();
                        if(line.contains("/**") || line.contains("/*")){
                            line = line.substring(0, line.indexOf("/*"));
                            willBeComment = true;
                        }
                        if(line.contains("}")){
                            line = line.substring(0, line.indexOf("}"));
                        }
                        if(!inComment && (line.contains("class ") || line.contains("interface ")) && (!line.substring(line.length() - 2, line.length()).equals("*/"))){
                            String className = null;
                            Vector<String> v = new Vector<String>();
                            String[] knownSuperclassesNames = null;
                            Scanner lineScan = new Scanner(line);
                            String[] words = line.split("[ ,{]+");
                            int k = 0;
                            for(int z = 0; z < words.length; z++){
                                if(words[z].equals("//")){
                                    break;
                                }else if(words[z].equals("/*") || words[z].equals("/**")){
                                    inComment = true;
                                    break;
                                }
                                else if(words[z].equals("class") || words[z].equals("interface"))
                                    k = 1;
                                else if(words[z].equals("extends"))
                                    k = 2;
                                else if(words[z].equals("implements"))
                                    k = 3;
                                else if(k == 1 && words[z] != ""){
                                    className = words[z];
                                    if(className.contains("<"))
                                        while(!className.contains(">") &&  z < words.length - 1){
                                            z++;
                                            className += words[z];
                                        }
                                    //System.out.println(className + " was added as a class.");
                                }
                                else if((k == 2 || k == 3) && words[z] != ""){
                                    String toAdd = "";
                                    toAdd += words[z];
                                    if(toAdd.contains("<"))
                                        while(!toAdd.contains(">") && z < words.length - 1){
                                            z++;
                                            toAdd += words[z];
                                        }
                                    v.add(toAdd);
                                    //System.out.println(words[z] + " is a superclass or super interface of " + className + ".");
                                }
                            }

                            knownSuperclassesNames = new String[v.size()];
                            for(int r = 0; r < v.size(); r ++){
                                knownSuperclassesNames[r] = v.get(r);
                            }
                            lineScan.close();
                            if(!inComment && className != null)
                                t.addClass(new ClassInfo(className, knownSuperclassesNames));
                        }else if(inComment){
                            if(line.contains("*/"))
                                inComment = false;
                        }else if(willBeComment){
                            willBeComment = false;
                            inComment = true;
                        }
                    }
                    sc.close();
                }
                
            }catch(FileNotFoundException e){
                System.out.println("File: " + fs + " was not found!");
            }
            
        }
        t.magic();
        Vector<String> vs = t.allCounted();
        Iterator<String> it = vs.iterator();
        while(it.hasNext()){
            System.out.print(it.next());
        }
    }
}