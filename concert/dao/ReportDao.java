package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.dto.SelectorDto;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.ProgrammRecord;
import net.thumbtack.school.concert.model.Song;

import java.util.*;


public interface ReportDao {
    Collection<Song> showAllAddedSongs() throws ServerException;

    List<Song> showSongsSelectedByComposer(String selector) throws ServerException;

    List<Song> showSongsSelectedByLyricAutor(String selector) throws ServerException;

    List<Song> showSongsSelectedBySinger(String selector) throws ServerException;

    LinkedList<ProgrammRecord> showCurrentProgramm() throws ServerException;
}
