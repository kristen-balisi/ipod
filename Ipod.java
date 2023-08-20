/* Name: Kristen Balisi   341426344
 * Date: 25/04/2023
 * Program Name: Ipod Music Player
 * Program Description: This Ipod music player application allows users 
 * to play songs using the play, pause, next, and previous buttons. The screen 
 * displays song information, including title, artist name, and album cover. 
 * Additionally, the program features an ‘add’ button for users to select and save 
 * a new song to their playlist by clicking a song entry from a table. The selected 
 * song will be placed after the currently playing song in their playlist. The 'delete' 
 * button removes the song currently displayed on the screen from the user's playlist.
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import sun.audio.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Ipod {
  public static void main(String[] args) {
    IpodDevice ipodDevice = new IpodDevice();
    ipodDevice.createAndDisplayScreen();
  }
}

class Audio {
  private InputStream inputStream;
  private AudioStream audioStream;
  
  Audio(String audioFile) {
    try {
      inputStream = new FileInputStream(audioFile);
      audioStream = new AudioStream(inputStream);
    }
    catch (Exception e) {
      if (audioFile == "no audio") {
        System.out.println("No song selected! Click the plus button to add songs to your playlist.\n");
      }
      else {
        System.out.println("Exception in loading audio file: " + e.toString());
      }
    }
  }
  
  public void play() {
    AudioPlayer.player.start(audioStream);
  }
  
  public void stop() {
    AudioPlayer.player.stop(audioStream);
  }
}

class Song {
  private int id;
  private String imgFile, title, artistName, audioFile;
  
  // parameterized constructor sets the value of instance variables
  Song(int id, String imgFile, String title, String artistName, String audioFile) {
    this.id = id;
    this.imgFile = imgFile;
    this.title = title;
    this.artistName = artistName;
    this.audioFile = audioFile;
  }
  
  // accessor methods get the value of instance variables
  public int getId() {
    return this.id;
  }
  
  public String getImgFile() {
    return this.imgFile;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public String getArtistName() {
    return this.artistName;
  }
  
  public String getAudioFile() {
    return this.audioFile;
  }
}

class IpodDevice extends JPanel implements MouseListener {
  // list of songs available in ipod device
  private List<Song> ipodSongList = getSongs();
  
  // list of songs selected by user
  private List<Song> userSongList = new LinkedList<Song>();
  
  private int songIndex = 0;
  
  // song that is displayed when playlist is empty
  private Song defaultSong;
  
  // create Audio class object
  private Audio audio;
  private boolean isSongPlaying = false;
  
  JFrame ipodFrame = new JFrame("Ipod Music Player - Kristen Balisi");
  JFrame selectSongFrame = new JFrame("Song Selection List - Kristen Balisi");
  
  // song images and labels
  ImageIcon artistImg;
  JLabel artistImgLabel = new JLabel("");
  JLabel songTitleLabel = new JLabel("");
  JLabel artistNameLabel = new JLabel("");
  
  // button images
  ImageIcon playButtonImg = new ImageIcon("playButton.png");
  ImageIcon pauseButtonImg = new ImageIcon("pauseButton.png");
  ImageIcon forwardButtonImg = new ImageIcon("forwardButton.png");
  ImageIcon prevButtonImg = new ImageIcon("previousButton.png");
  ImageIcon addButtonImg = new ImageIcon("addButton.png");
  ImageIcon deleteButtonImg = new ImageIcon("deleteButton.png");
  
  JLabel playButtonLabel = new JLabel(playButtonImg);
  JLabel forwardButtonLabel = new JLabel(forwardButtonImg);
  JLabel prevButtonLabel = new JLabel(prevButtonImg);
  JLabel addButtonLabel = new JLabel(addButtonImg);
  JLabel deleteButtonLabel = new JLabel(deleteButtonImg);
  
  JLabel selectSongLabel = new JLabel("*To SAVE, click desired song. To CANCEL, click X button.");
  
  // reference box for MouseListener events on ipod frame
  JLabel referenceBox = new JLabel("");
  
  IpodDevice() {
    this.addMouseListener(this); 
    
    defaultSong = new Song(0, "noSong.png", "No Song Selected", "No Artist", "no audio");
    
    if (userSongList.size() == 0) {
      userSongList.add(defaultSong);
    } 
  }
  
  public void createAndDisplayScreen() {
    int frameWidth = 600;
    int frameHeight = 800;
    
    // song information - frame 1
    ipodFrame.setSize(frameWidth, frameHeight);
    ipodFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ipodFrame.setIconImage(new ImageIcon("musicNote.png").getImage());
    
    JPanel ipodPanel = new JPanel();
    ipodPanel.setBackground(Color.white);
    ipodFrame.add(ipodPanel);
    placeScreenComponents(ipodPanel);  
    ipodPanel.setFocusable(true);
    
    ipodFrame.setLocationRelativeTo(null);
    ipodFrame.setVisible(true);
    
    // display song data based on current song index
    displaySongInformation(userSongList.get(songIndex));
    
    // song selection list - frame 2
    selectSongFrame.setSize(frameWidth, frameHeight - 250);
    selectSongFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    selectSongFrame.setIconImage(new ImageIcon("musicNote.png").getImage());
    
    JPanel selectSongPanel = new JPanel();
    selectSongPanel.setBackground(Color.white);
    selectSongFrame.add(selectSongPanel);
    placeScreenComponents2(selectSongPanel);  
    selectSongPanel.setFocusable(true);
    
    selectSongFrame.setLocationRelativeTo(null);   
  }
  
  public void placeScreenComponents(JPanel ipodPanel) {
    ipodPanel.setLayout(null);
    
    // artist - image
    artistImgLabel.setBounds(65, 50, 450, 450);
    ipodPanel.add(artistImgLabel);
    
    // song title - label
    songTitleLabel.setBounds(65, 520, 400, 30);
    songTitleLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
    ipodPanel.add(songTitleLabel);
    
    // artist name - label
    artistNameLabel.setBounds(65, 550, 400, 30);
    artistNameLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
    ipodPanel.add(artistNameLabel);
    
    // buttons - images
    playButtonLabel.setBounds(250, 600, 100, 100);
    ipodPanel.add(playButtonLabel);
    forwardButtonLabel.setBounds(345, 600, 100, 100);
    ipodPanel.add(forwardButtonLabel);
    prevButtonLabel.setBounds(150, 600, 100, 100);
    ipodPanel.add(prevButtonLabel);
    
    addButtonLabel.setBounds(490,625,25,25);
    ipodPanel.add(addButtonLabel);
    deleteButtonLabel.setBounds(490,665,25,25);
    ipodPanel.add(deleteButtonLabel);
    
    // box used as a reference point for MouseListener events
    referenceBox.setBounds(0, 0, 600, 800);
    ipodPanel.add(referenceBox);
    referenceBox.addMouseListener(this);
  }
  
  private List<Song> getSongs() {
    List<Song> songs = new LinkedList<Song>();
    songs.add(new Song(1, "laufey.jpeg", "Let You Break My Heart Again", "Laufey", "Song1.wav"));
    songs.add(new Song(2, "sam.jpg", "Love Me Like That", "Sam Kim", "Song2.wav"));
    songs.add(new Song(3, "daniel.jpg", "Blessed", "Daniel Caesar", "Song3.wav"));
    songs.add(new Song(4, "niki.jpg", "La La Lost You", "Niki", "Song4.wav"));
    songs.add(new Song(5, "olivia.jpg", "Hope You're Okay", "Olivia Rodrigo", "Song5.wav"));
    
    return songs;
  }
  
  public TableModel getSongData() {
    // set table column names for song selection list
    String columnNames[] = {"ID", "Song Title", "Artist Name"};
    
    // create table model object to manage table values
    DefaultTableModel songListTableModel = new DefaultTableModel(columnNames, 0);
    
    // populate table rows using information retrieved from ipod song linked list
    List<Song> ipodSongList = getSongs();
    
    for (int i = 0; i < ipodSongList.size(); i++) {
      int id = ipodSongList.get(i).getId();
      String songTitle = ipodSongList.get(i).getTitle();
      String artistName = ipodSongList.get(i).getArtistName();
      
      // add song id, title, and artist to table row
      Object[] songData = {id, songTitle, artistName};
      songListTableModel.addRow(songData);
    }
    return songListTableModel;
  }
  
  public void placeScreenComponents2(JPanel selectSongPanel) {
    selectSongFrame.add(selectSongPanel);
    
    // create bordered title
    selectSongPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Song List"));
    TitledBorder selectSongBorder = (TitledBorder)selectSongPanel.getBorder();
    selectSongBorder.setTitleFont(new Font("Helvetica", Font.BOLD, 16));
    
    // create table using table model
    TableModel data = getSongData();
    JTable songListTable = new JTable(data);
    selectSongPanel.add(new JScrollPane(songListTable));
    songListTable.addMouseListener(this);
    
    // save/cancel comment - label
    selectSongLabel.setBounds(100, 550, 100, 100);
    selectSongLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
    selectSongLabel.setForeground(Color.red);
    selectSongPanel.add(selectSongLabel);
    
    selectSongFrame.setVisible(false);
  }
  
  public Song getSongById(List<Song> songs, int id) {
    for (int i = 0; i < songs.size(); i++) {
      if (songs.get(i).getId() == id) {
        // return song from ipod song list at indicated id
        return songs.get(i);
      }
    }
    return null;
  }
  
  public void addSong(Song song) {
    // stop currently playing song before adding new song
    stopAudio();
    
    if (userSongList.size() == 1) {
      // remove default song when the first song is added to the playlist
      userSongList.remove(defaultSong);
      
      // add first song at start of playlist
      songIndex = 0;
      userSongList.add(songIndex, song);
    }
    else {
      // if the user song list size is greater than 1, add 
      // new song after currently playing song in playlist
      userSongList.add(songIndex+1, song);
      songIndex++;
    }
    
    selectSongFrame.setVisible(false);
    
    playAudio();
    System.out.println("'" + song.getTitle() + "' by " + song.getArtistName() + " has been added to your playlist!\n");
  }
  
  public void deleteSong(Song song) {
    stopAudio();
    
    userSongList.remove(song);
    
    if (userSongList.size() > 0) {
      // set index to last song in playlist after song is deleted
      songIndex = userSongList.size() - 1;
      System.out.println("'" + song.getTitle() + "' by " + song.getArtistName() + " has been removed from your playlist!\n");
    }
    else {
      // if the user's song list has 0 songs, add default song
      userSongList.add(defaultSong);
    }
    
    playAudio();
  }
  
  public void displaySongInformation(Song song) {
    artistImg = new ImageIcon(song.getImgFile());
    artistImgLabel.setIcon(artistImg); 
    
    songTitleLabel.setText(song.getTitle());
    
    artistNameLabel.setText(song.getArtistName());   
  }
  
  public void togglePlayPause() {
    // if user clicks play button and song is already playing, set image to pause button
    if (isSongPlaying) {
      playButtonLabel.setIcon(pauseButtonImg);
    }
    else {
      playButtonLabel.setIcon(playButtonImg);
    }   
  }
  
  // method called when play button pressed
  public void playAudio() {
    Song song = userSongList.get(songIndex);
    
    if (isSongPlaying) {
      // stop audio of the previously playing song
      stopAudio();
    } 
    else {
      isSongPlaying = true;
      displaySongInformation(song);
      audio = new Audio(song.getAudioFile());
      audio.play(); 
    }
    togglePlayPause();
  }
  
  public void stopAudio() {
    // stop previous song to prevent overlapping audios
    if (audio != null) {
      audio.stop();
    }
    
    isSongPlaying = false;
  }
  
  public void nextAudio() {
    int maxSong = userSongList.size() - 1;
    
    stopAudio();
    
    if (songIndex < maxSong) {
      songIndex++;
    } 
    else {
      // if user reaches end of playlist, set index to the start
      songIndex = 0;
    }
    
    playAudio();
  }
  
  public void previousAudio() {
    int maxSong = userSongList.size() - 1;
    
    stopAudio();
    
    if (songIndex > 0) {
      songIndex--;
    } 
    else {
      // if user reaches start of playlist, set index to the end
      songIndex = maxSong;
    }
    
    playAudio();
  }
  
  // print songs for debugging purposes
  public void printSongList(List<Song> songs) {
    for (int i = 0; i < songs.size(); i++) {
      System.out.println(songs.get(i).getTitle());
    }
  }
  
  // checks if selected song is already in user's playlist
  public boolean isSongDuplicated(List<Song> songs, int id) {
    for (int i = 0; i < songs.size(); i++) {
      if (songs.get(i).getId() == id) {
        return true;
      }
    }
    return false;
  }
  
  // method called when JTable on second frame clicked
  public void handleTableClick(MouseEvent e) {
    // convert object on which the event occurred into a JTable
    JTable selectSongTable = (JTable)e.getSource();
    
    // get row based on user table click
    int row = selectSongTable.rowAtPoint(e.getPoint());
    // column 0 corresponds to the song id column
    int col = 0;
    
    // retrieve song id from column 0 of the clicked row in the table
    Object songId = selectSongTable.getValueAt(row, col);
    
    Song selectedSong = getSongById(ipodSongList, (int)songId);
    
    if (!isSongDuplicated(userSongList, (int)songId)) {
      addSong(selectedSong);
    } 
    else {
      System.out.println("'" + selectedSong.getTitle() + "' by " + selectedSong.getArtistName() + " is already in your playlist.\n");
    }
    selectSongFrame.setVisible(false);
  }
  
  public void mouseClicked(MouseEvent e) {
    
    // if user clicks on the song selection table in the second GUI frame, call corresponding method 
    if (e.getSource().getClass() == JTable.class) {
      handleTableClick(e);
      return;
    }
    
    double clickedX = e.getX();
    double clickedY = e.getY();
    
    // call corresponding methods based on the user's mouse click coordinates
    if ((clickedX >= 250 && clickedX <= 350) && (clickedY >= 600 && clickedY <= 700)) {
      playAudio();
    }
    
    if ((clickedX >= 345 && clickedX <= 445) && (clickedY >= 600 && clickedY <= 700)) {
      nextAudio();
    }
    
    if ((clickedX >= 150 && clickedX <= 250) && (clickedY >= 600 && clickedY <= 700)) {
      previousAudio();
    }
    
    if ((clickedX >= 490 && clickedX <= 515) && (clickedY >= 625 && clickedY <= 650)) {
      selectSongFrame.setVisible(true);
    }
    
    if ((clickedX >= 490 && clickedX <= 515) && (clickedY >= 665 && clickedY <= 690)) {
      deleteSong(userSongList.get(songIndex));
    }
  }
  
  public void mousePressed(MouseEvent e) {}
  
  public void mouseReleased(MouseEvent e) {}
  
  public void mouseEntered(MouseEvent e) {}
  
  public void mouseExited(MouseEvent e) {}
}