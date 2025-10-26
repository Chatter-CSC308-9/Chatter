package main.controllers;

import main.boundaries.screens.CurrentEdit;
import main.entities.Project;

import java.io.*;


// NOTE: PATHS TO TITLE AND WORK FILES ARE HARDCODED EXCEPT NAME OF PROJECT FOLDER


public class EditProjectController {
    CurrentEdit currentEditBoundary;
    String projectFolder;
    Project project;

    public void setCurrentEditBoundary(CurrentEdit ceb) {
        this.currentEditBoundary = ceb;
    }

    public void editProject(String projectFolder) {
        this.projectFolder = projectFolder;
        // use json to load information into project entity
    }

    // return title of project
    public String getTitle() {
        String title = "hello world";
        try(BufferedReader br = new BufferedReader(new FileReader("server/" + projectFolder + "/title.txt"))) {
            title = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return title;
    }

    // return text of project
    public String getWork() {
        String everything = "hello world";
        try(BufferedReader br = new BufferedReader(new FileReader("server/" + projectFolder + "/work.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return everything;
    }

    // save title and work
    public void saveWork(String title, String work) {
        // write to title file
        try (FileWriter writer = new FileWriter("server/" + projectFolder + "/title.txt")) {
            writer.write(title);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to work file
        try (FileWriter writer = new FileWriter("server/" + projectFolder + "/work.txt")) {
            writer.write(work);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
