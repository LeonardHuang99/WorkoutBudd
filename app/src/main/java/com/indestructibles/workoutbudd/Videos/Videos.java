package com.indestructibles.workoutbudd.Videos;

/**
 * Created by Léonard Huang on 12/06/2021.
 **/
public class Videos {
    int Image;
    String Description;
    String Author;
    String Link;

    public Videos(int image, String description, String author, String link) {
        Image = image;
        Description = description;
        Author = author;
        Link = link;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }
}
