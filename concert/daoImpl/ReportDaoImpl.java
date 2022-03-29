package net.thumbtack.school.concert.daoImpl;

import net.thumbtack.school.concert.dao.ReportDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.ProgrammRecord;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.SongRating;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportDaoImpl implements ReportDao {

    @Override
    public Collection<Song> showAllAddedSongs() throws ServerException {
        if (Database.getInstance().getSongsBase().isEmpty()) {
            throw new ServerException(ServerErrorCode.SONGS_BASE_IS_EMPTY);
        } else {
            return Database.getInstance().getSongsBase().values();
        }
    }

    @Override
    public LinkedList<ProgrammRecord> showCurrentProgramm() throws ServerException {
        if (Database.getInstance().getSongsBase().isEmpty()) {
            throw new ServerException(ServerErrorCode.SONGS_BASE_IS_EMPTY);
        } else {

            SortedMap<ProgrammRecord, Integer> data = new TreeMap<ProgrammRecord, Integer>
                    (
                            new Comparator<ProgrammRecord>() {
                                public int compare(ProgrammRecord r1, ProgrammRecord r2) {
                                    int res = -r1.getAverageRating().compareTo(r2.getAverageRating());
                                    if (res != 0) {
                                        return -r1.getAverageRating().compareTo(r2.getAverageRating());
                                    } else {
                                        return r1.getSongName().compareTo(r2.getSongName());
                                    }
                                }
                            }
                    );

            for (Song i : Database.getInstance().getSongsBase().values()) {
                Double averageRating = 0.0;

                if (!i.getRatings().isEmpty()) {
                    Double sumRating = 0.0;
                    for (SongRating j : i.getRatings().values()) {
                        sumRating += j.getRating();
                    }
                    averageRating = sumRating / i.getRatings().size();
                }

                ProgrammRecord newRecord = new ProgrammRecord
                        (i.getSongName(), i.getComposerName(), i.getLyricAutorName(),
                                i.getSingerName(), i.getUserWhoProposed(), averageRating,
                                i.getSongsComments());
                data.put(newRecord, i.getDuration());
            }


            int currentDuration = 0;
            LinkedList<ProgrammRecord> result = new LinkedList<ProgrammRecord>();

            for (ProgrammRecord r : data.keySet()) {
                int newDuration = currentDuration + 10 + data.get(r);
                if (newDuration >= 3600) {
                    continue;
                } else {
                    currentDuration = currentDuration + 10 + data.get(r);
                    result.add(r);
                }
            }
            return result;
        }
    }

    @Override
    public List<Song> showSongsSelectedByComposer(String selector) throws ServerException {

        if (Database.getInstance().getSongsBase().isEmpty()) {
            throw new ServerException(ServerErrorCode.SONGS_BASE_IS_EMPTY);
        } else {

            //Map<String, List<Song>> songsByComposer = Database.getInstance().getSongsBase().values().stream().
            //       collect(Collectors.groupingBy(Song::getComposerName));

            if (Database.getInstance().getSongsByComposer().get(selector) == null ||
                    Database.getInstance().getSongsByComposer().get(selector).isEmpty()) {
                throw new ServerException(ServerErrorCode.NO_SONGS_FOUND);
            } else {
                return new ArrayList<Song>(Database.getInstance().getSongsByComposer().get(selector));
            }
        }
    }

    @Override
    public List<Song> showSongsSelectedByLyricAutor(String selector) throws ServerException {

        if (Database.getInstance().getSongsBase().isEmpty()) {
            throw new ServerException(ServerErrorCode.SONGS_BASE_IS_EMPTY);
        } else {
            //Map<String, List<Song>> songsByLyricAutor = Database.getInstance().getSongsBase().values().stream().
            //        collect(Collectors.groupingBy(Song::getLyricAutorName));

            if (Database.getInstance().getSongsByLyricAutor().get(selector) == null || Database.getInstance().getSongsByLyricAutor().get(selector).isEmpty()) {
                throw new ServerException(ServerErrorCode.NO_SONGS_FOUND);
            } else {
                return new ArrayList<Song> (Database.getInstance().getSongsByLyricAutor().get(selector));
            }
        }
    }

    @Override
    public List<Song> showSongsSelectedBySinger(String selector) throws ServerException {

        if (Database.getInstance().getSongsBase().isEmpty()) {
            throw new ServerException(ServerErrorCode.SONGS_BASE_IS_EMPTY);
        } else {
           // Map<String, List<Song>> songsBySinger = Database.getInstance().getSongsBase().values().stream().
           //         collect(Collectors.groupingBy(Song::getSingerName));

            if (Database.getInstance().getSongsBySinger().get(selector) == null || Database.getInstance().getSongsBySinger().get(selector).isEmpty()) {
                throw new ServerException(ServerErrorCode.NO_SONGS_FOUND);
            } else {
                return new ArrayList<Song> (Database.getInstance().getSongsBySinger().get(selector));
            }
        }
    }
}
