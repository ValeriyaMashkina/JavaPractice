package net.thumbtack.school.springPractice.model;

public class Recording {

    private String artist;
    private String composition;
    private String album;
    private int year;
    private String genre;
    private int duration;
    private String audioLink;
    private String videoLink;

    public Recording() {
    }

    public Recording(String artist, String composition, String album, int year, String genre,
                     int duration, String audioLink, String videoLink) {
        this.artist = artist;
        this.composition = composition;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.duration = duration;
        this.audioLink = audioLink;
        this.videoLink = videoLink;
    }

   // public Recording(SaveRecordingRequestDto saveRecordingRequestDto) {
      //  this(saveRecordingRequestDto.getArtist(),
     //           saveRecordingRequestDto.getComposition(),
     //           saveRecordingRequestDto.getAlbum(),
      //          saveRecordingRequestDto.getYear(),
       //         saveRecordingRequestDto.getGenre(),
      //          saveRecordingRequestDto.getDuration(),
      //          saveRecordingRequestDto.getAudioLink(),
      //          saveRecordingRequestDto.getVideoLink());
   // }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }



}
