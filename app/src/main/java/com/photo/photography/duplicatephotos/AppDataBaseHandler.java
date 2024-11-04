package com.photo.photography.duplicatephotos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AppDataBaseHandler extends SQLiteOpenHelper {
    // Records table name
    public static final String TABLE_DUPLICATE = "DuplicatePhotos";
    // Records Table TABLE_DUPLICATE Columns name
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CHECK_BOX = "groupSetCheckBox";
    public static final String COLUMN_IS_SIMILAR = "isSimilar";
    public static final String COLUMN_GROUP_TAG = "groupTag";
    public static final String COLUMN_IND_GRP_OF_DUPES = "individualGrpOfDupes";
    // Database name
    private static final String DATABASE = "DuplicateDB";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // All Static variables
    private static final String TAG = "AppDBHandler";
    public static boolean isDebugMode = true;

    public AppDataBaseHandler(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public void printLog(String msg) {
        if (isDebugMode)
            Log.e(TAG, msg);
    }

    //TODO Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DUPLICATE + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CHECK_BOX + " TEXT,"
                    + COLUMN_IS_SIMILAR + " TEXT,"
                    + COLUMN_GROUP_TAG + " TEXT,"
                    + COLUMN_IND_GRP_OF_DUPES + " TEXT)";
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DUPLICATE);

        // Create tables again
        onCreate(db);
    }

    //TODO Insert or Update Group data
    public void insertUpdateGroupList(List<IndividualGroups> list, boolean isSimilar) {
        Log.e(TAG, "==================== INSERT START ====================");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                IndividualGroups info = list.get(i);
                ContentValues values = new ContentValues();
                values.put(COLUMN_CHECK_BOX, "" + info.isGroupSetCheckBox());
                values.put(COLUMN_IS_SIMILAR, "" + isSimilar);
                values.put(COLUMN_GROUP_TAG, info.getGroupTag());
                values.put(COLUMN_IND_GRP_OF_DUPES, new Gson().toJson(info.getIndividualGrpOfDupes()));

                if (info.getDbId() != -1) {
                    Cursor cursor = db.query(TABLE_DUPLICATE, null, COLUMN_ID + " = ? ", new String[]{String.valueOf(info.getDbId())}, null, null, null, null);
                    int total = cursor.getCount();
                    if (total > 0) {
                        // Updating Row
                        int log = db.update(TABLE_DUPLICATE, values, COLUMN_ID + " = ? ", new String[]{String.valueOf(info.getDbId())});
                        printLog("UPDATE GROUP isSimilar : " + isSimilar);

                    } else {
                        // Inserting Row
                        long log = db.insert(TABLE_DUPLICATE, null, values);
                        printLog("INSERT GROUP isSimilar : " + isSimilar);
                    }

                } else {
                    // Inserting Row
                    long log = db.insert(TABLE_DUPLICATE, null, values);
                    printLog("INSERT GROUP isSimilar : " + isSimilar);
                }
            }
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On GROUP Data INSERT UPDATE Query. " + exc.toString());
        }
        Log.e(TAG, "==================== INSERT DONE ====================");
    }

    //TODO Get all Group data
    public List<IndividualGroups> getAllItems(boolean isSimilar) {
        Log.e(TAG, "==================== GET ALL START ====================");
        String selection = COLUMN_IS_SIMILAR + " = ? ";
        String[] selectionArgs = new String[]{"" + isSimilar};
        String[] selColumns = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor filesCursor = db.query(TABLE_DUPLICATE, selColumns, selection, selectionArgs, groupBy, having, orderBy);
        if (filesCursor == null) {
            Log.e(TAG, "Cursor is null");
            return new ArrayList<>();
        }

        Log.e(TAG, "Total " + filesCursor.getCount() + " Records retrieved");
        List<IndividualGroups> list = toList(filesCursor);
        filesCursor.close();
        Log.e(TAG, "==================== GET ALL DONE ====================");

        Log.e(TAG, "==================== FILTER SINGLE MEDIA START ====================");
        List<IndividualGroups> filteredList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIndividualGrpOfDupes().size() < 2) {
                Log.e(TAG, "Object Contains Single media so Delete this object");
                removeSingleMediaRecord(String.valueOf(list.get(i).getDbId()));
            } else {
                filteredList.add(list.get(i));
                Log.e(TAG, "Object Contains Multiple media");
            }
        }
        Log.e(TAG, "Filtered Total " + filesCursor.getCount() + " Records retrieved");
        Log.e(TAG, "==================== FILTER SINGLE MEDIA DONE ====================");

        Log.e(TAG, "==================== UPDATE GROUP TAG START ====================");
        if (list.size() != filteredList.size()) {
            for (int i = 0; i < filteredList.size(); i++) {
                IndividualGroups group = filteredList.get(i);
                group.setGroupTag(i + 1);
                for (int p = 0; p < group.getIndividualGrpOfDupes().size(); p++) {
                    group.getIndividualGrpOfDupes().get(p).setImageItemGrpTag(i);
                }
                updateGroup(group, isSimilar);
            }
        }
        Log.e(TAG, "==================== UPDATE GROUP TAG DONE ====================");
        return filteredList;
    }

    //TODO Insert or Update Group data
    public void updateGroup(IndividualGroups group, boolean isSimilar) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHECK_BOX, "" + group.isGroupSetCheckBox());
            values.put(COLUMN_IS_SIMILAR, "" + isSimilar);
            values.put(COLUMN_GROUP_TAG, group.getGroupTag());
            values.put(COLUMN_IND_GRP_OF_DUPES, new Gson().toJson(group.getIndividualGrpOfDupes()));

            if (group.getDbId() != -1) {
                Cursor cursor = db.query(TABLE_DUPLICATE, null, COLUMN_ID + " = ? ", new String[]{String.valueOf(group.getDbId())}, null, null, null, null);
                int total = cursor.getCount();
                if (total > 0) {
                    // Updating Row
                    int log = db.update(TABLE_DUPLICATE, values, COLUMN_ID + " = ? ", new String[]{String.valueOf(group.getDbId())});
                    printLog("UPDATE GROUP isSimilar : " + log);
                }
            }
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On GROUP Data INSERT UPDATE Query. " + exc.toString());
        }
    }

    //TODO Get all Group data
    public void removeAllRecord() {
        Log.e(TAG, "==================== REMOVE ALL DATA ====================");
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(TABLE_DUPLICATE, null, null);
        Log.e(TAG, "Remove " + success + " Rows");
        Log.e(TAG, "==================== REMOVE ALL DATA ====================");
    }

    //TODO Get all Group data
    public void removeSingleMediaRecord(String column_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(TABLE_DUPLICATE, COLUMN_ID + " = ? ", new String[]{column_id});
        Log.e(TAG, "Remove " + success + " Rows");
    }

    private List<IndividualGroups> toList(Cursor itemsCursor) {
        List<IndividualGroups> itemList = new ArrayList<IndividualGroups>();
        if (itemsCursor.moveToFirst()) {
            do {
                IndividualGroups item = cursorToItemPackage(itemsCursor);
                itemList.add(item);
                itemsCursor.moveToNext();
            } while (!itemsCursor.isAfterLast());
        }
        return itemList;
    }

    @SuppressLint("Range")
    private IndividualGroups cursorToItemPackage(Cursor itemsCursor) {
        IndividualGroups item = new IndividualGroups();
        item.setDbId(itemsCursor.getLong(itemsCursor.getColumnIndex(COLUMN_ID)));
        item.setGroupSetCheckBox(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_CHECK_BOX)).equalsIgnoreCase("true"));
        item.setGroupTag(itemsCursor.getInt(itemsCursor.getColumnIndex(COLUMN_GROUP_TAG)));
        String json = itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_IND_GRP_OF_DUPES));
        item.setIndividualGrpOfDupes(new Gson().fromJson(json, new TypeToken<ArrayList<ImagesItem>>() {
        }.getType()));
        return item;
    }


/*
    //TODO Get all matches
    public ArrayList<SMMatch> getMatchListByCompIdSeason(String seasonId) {

        printLog("Start getMatchListByCompIdSeason");
        ArrayList<SMMatch> matchesList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.query(TABLE_MATCH, null, KEY_SEASON_ID + " = ? ",
                    new String[]{String.valueOf(seasonId)}, null, null, null, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SMMatch records = new SMMatch();
                    String MATCH_ID = cursor.getString(cursor.getColumnIndex(KEY_MATCH_ID));
                    String LEAGUE_ID = cursor.getString(cursor.getColumnIndex(KEY_LEAGUE_ID));
                    String SEASON_ID = cursor.getString(cursor.getColumnIndex(KEY_SEASON_ID));
                    String STAGE_ID = cursor.getString(cursor.getColumnIndex(KEY_STAGE_ID));
                    String ROUND_ID = cursor.getString(cursor.getColumnIndex(KEY_ROUND_ID));
                    String GROUP_ID = cursor.getString(cursor.getColumnIndex(KEY_GROUP_ID));
                    String AGGREGATE_ID = cursor.getString(cursor.getColumnIndex(KEY_AGGREGATE_ID));
                    String LOCALTEAM_ID = cursor.getString(cursor.getColumnIndex(KEY_LOCALTEAM_ID));
                    String VISITORTEAM_ID = cursor.getString(cursor.getColumnIndex(KEY_VISITORTEAM_ID));

                    records.setMatchId(!TextUtils.isEmpty(MATCH_ID) ? Long.parseLong(MATCH_ID) : null);
                    records.setLeague_id(!TextUtils.isEmpty(LEAGUE_ID) ? Long.parseLong(LEAGUE_ID) : null);
                    records.setSeason_id(!TextUtils.isEmpty(SEASON_ID) ? Long.parseLong(SEASON_ID) : null);
                    records.setStage_id(!TextUtils.isEmpty(STAGE_ID) ? Long.parseLong(STAGE_ID) : null);
                    records.setRound_id(!TextUtils.isEmpty(ROUND_ID) ? Long.parseLong(ROUND_ID) : null);
                    records.setGroup_id(!TextUtils.isEmpty(GROUP_ID) ? Long.parseLong(GROUP_ID) : null);
                    records.setAggregate_id(!TextUtils.isEmpty(AGGREGATE_ID) ? Long.parseLong(AGGREGATE_ID) : null);
                    records.setLocalteam_id(!TextUtils.isEmpty(LOCALTEAM_ID) ? Long.parseLong(LOCALTEAM_ID) : null);
                    records.setVisitorteam_id(!TextUtils.isEmpty(VISITORTEAM_ID) ? Long.parseLong(VISITORTEAM_ID) : null);

                    Gson gson = new Gson();
                    Formations formattions = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_FORMATION)), Formations.class);
                    records.setFormations(formattions);

                    gson = new Gson();
                    Scores scores = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_SCORES)), Scores.class);
                    records.setScores(scores);

                    gson = new Gson();
                    Time time = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_TIME)), Time.class);
                    records.setTime(time);
                    records.setDeleted(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_DELETED))));

                    gson = new Gson();
                    LocalTeam localTeam = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_LOCAL_TEAM)), LocalTeam.class);
                    records.setLocalTeam(localTeam);

                    gson = new Gson();
                    VisitorTeam visitorTeam = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_VISITOR_TEAM)), VisitorTeam.class);
                    records.setVisitorTeam(visitorTeam);

                    gson = new Gson();
                    League league = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_LEAGUE)), League.class);
                    records.setLeague(league);

                    gson = new Gson();
                    Round round = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_ROUND)), Round.class);
                    records.setRound(round);

                    gson = new Gson();
                    Stage stage = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_STAGE)), Stage.class);
                    records.setStage(stage);

                    gson = new Gson();
                    Aggregate aggregate = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_AGGREGATE)), Aggregate.class);
                    records.setAggregate(aggregate);

                    // convert date and time with current selected timezone wise
                    String matchDate = SupportedClass.getDateInFormat(records.getTime().getStarting_at().getDate(), records.getTime().getStarting_at().getTime());
                    String matchtime = SupportedClass.getTimeInFormat(records.getTime().getStarting_at().getDate(), records.getTime().getStarting_at().getTime());
                    records.getTime().getStarting_at().setTimeLocal(matchtime);
                    records.getTime().getStarting_at().setDateLocal(matchDate);

                    // Adding records to list
                    matchesList.add(records);
                }
                while (cursor.moveToNext());
            }
            db.close();
            printLog("End getMatchListByCompIdSeason");
            printLog("getMatchListByCompIdSeason data list size : " + matchesList.size());
            return matchesList;
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On getMatchListByCompIdSeason data " + exc.toString());
            return null;
        }
    }

    //TODO Update Matches data
    public void updateMatchDataForWeekDisplay(SMMatch records) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            Gson gson = new Gson();
            String statsJson = gson.toJson(records.getStats());
            values.put(KEY_STATS, !TextUtils.isEmpty(statsJson) ? statsJson : "");

            gson = new Gson();
            String scoresJson = gson.toJson(records.getScores());
            values.put(KEY_SCORES, !TextUtils.isEmpty(scoresJson) ? scoresJson : "");

            gson = new Gson();
            String timeJson = gson.toJson(records.getTime());
            values.put(KEY_TIME, !TextUtils.isEmpty(timeJson) ? timeJson : "");

            gson = new Gson();
            String localTeamJson = gson.toJson(records.getLocalTeam());
            values.put(KEY_LOCAL_TEAM, !TextUtils.isEmpty(localTeamJson) ? localTeamJson : "");
            gson = new Gson();
            String visitorTeamJson = gson.toJson(records.getVisitorTeam());
            values.put(KEY_VISITOR_TEAM, !TextUtils.isEmpty(visitorTeamJson) ? visitorTeamJson : "");

            gson = new Gson();
            String aggregateJson = gson.toJson(records.getAggregate());
            values.put(KEY_AGGREGATE, !TextUtils.isEmpty(aggregateJson) ? aggregateJson : "");

            Cursor cursor = db.query(TABLE_MATCH, new String[]{KEY_LEAGUE_ID},
                    KEY_MATCH_ID + " = ? AND " + KEY_LEAGUE_ID + " = ? AND " + KEY_SEASON_ID + " = ?",
                    new String[]{
                            String.valueOf(records.getMatchId()), String.valueOf(records.getLeague_id()),
                            String.valueOf(records.getSeason_id())
                    }, null, null, null, null);

            int total = cursor.getCount();
            if (total > 0) {
                // Updating Row
                db.update(TABLE_MATCH, values,
                        KEY_MATCH_ID + " = ? AND " + KEY_LEAGUE_ID + " = ? AND " + KEY_SEASON_ID + " = ? ",
                        new String[]{String.valueOf(records.getMatchId()), String.valueOf(records.getLeague_id()),
                                String.valueOf(records.getSeason_id())});
                printLog("UPDATE Match Live Data " + records.getMatchId() + " => " + records.getLocalTeam().getTeamData().getName() + " - " + records.getVisitorTeam().getTeamData().getName());

            }
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On Data Matches INSERT UPDATE Query. " + exc.toString());
        }
    }

    //TODO Insert or Update League data
    public void addUpdateLeagueList(List<LeagueData> competitionList) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < competitionList.size(); i++) {
                LeagueData records = competitionList.get(i);
                ContentValues values = new ContentValues();
                values.put(KEY_LEAGUE_ID, records.getId() != null ? records.getId() + "" : "");
                values.put(KEY_PRIORITY, records.getPriority() != null ? records.getPriority() + "" : "");
                values.put(KEY_LEGACY_ID, records.getLegacy_id() != null ? records.getLegacy_id() + "" : "");
                values.put(KEY_COUNTRY_ID, records.getCountry_id() != null ? records.getCountry_id() + "" : "");
                values.put(KEY_NAME, records.getName() != null ? records.getName() + "" : "");
                values.put(KEY_IS_CUP, records.getIs_cup() != null ? records.getIs_cup() + "" : "");
                values.put(KEY_CURR_SEASON_ID, records.getCurrent_season_id() != null ? records.getCurrent_season_id() + "" : "");
                values.put(KEY_CURR_ROUND_ID, records.getCurrent_round_id() != null ? records.getCurrent_round_id() + "" : "");
                values.put(KEY_CURR_STAGE_ID, records.getCurrent_stage_id() != null ? records.getCurrent_stage_id() + "" : "");
                values.put(KEY_LIVE_STANDINGS, records.getLive_standings() != null ? records.getLive_standings() + "" : "");
                Gson gson = new Gson();
                String countryJson = gson.toJson(records.getCountry() != null ? records.getCountry() : "");
                values.put(KEY_COUNTRY, !TextUtils.isEmpty(countryJson) ? countryJson : "");

                Cursor cursor = db.query(TABLE_LEAGUE, null, KEY_LEAGUE_ID + " = ?",
                        new String[]{String.valueOf(records.getId())}, null, null, null, null);

                int total = cursor.getCount();
                if (total > 0) {
                    // Updating Row
                    db.update(TABLE_LEAGUE, values, KEY_LEAGUE_ID + " = ? ", new String[]{String.valueOf(records.getId())});
                    printLog("UPDATE League " + (i + 1) + " with Id : " + records.getId() + " => " + records.getName() + " - " + records.getName());

                } else {
                    // Inserting Row
                    db.insert(TABLE_LEAGUE, null, values);
                    printLog("INSERT League " + (i + 1) + " with Id : " + records.getId() + " => " + records.getName() + " - " + records.getName());
                }
            }
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On League Data INSERT UPDATE Query. " + exc.toString());
        }
    }

    //TODO Get all League data
    public ArrayList<LeagueData> getAllLeagueList() {

        ArrayList<LeagueData> leagueList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.query(TABLE_LEAGUE, null, null, null, null,
                    null, KEY_LEAGUE_ID, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    LeagueData records = new LeagueData();
                    String LEAGUE_ID = cursor.getString(cursor.getColumnIndex(KEY_LEAGUE_ID));
                    records.setId(!TextUtils.isEmpty(LEAGUE_ID) ? Long.parseLong(LEAGUE_ID) : null);
                    String PRIORITY = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
                    records.setPriority(!TextUtils.isEmpty(PRIORITY) ? Integer.parseInt(PRIORITY) : 0);
                    String LEGACY_ID = cursor.getString(cursor.getColumnIndex(KEY_LEGACY_ID));
                    records.setLegacy_id(!TextUtils.isEmpty(LEGACY_ID) ? Long.parseLong(LEGACY_ID) : null);
                    String COUNTRY_ID = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_ID));
                    records.setCountry_id(!TextUtils.isEmpty(COUNTRY_ID) ? Long.parseLong(COUNTRY_ID) : null);
                    String NAME = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                    records.setName(!TextUtils.isEmpty(NAME) ? NAME : null);
                    String IS_CUP = cursor.getString(cursor.getColumnIndex(KEY_IS_CUP));
                    records.setIs_cup(!TextUtils.isEmpty(IS_CUP) ? Boolean.parseBoolean(IS_CUP) : null);
                    String CURR_SEASON_ID = cursor.getString(cursor.getColumnIndex(KEY_CURR_SEASON_ID));
                    records.setCurrent_season_id(!TextUtils.isEmpty(CURR_SEASON_ID) ? Long.parseLong(CURR_SEASON_ID) : null);
                    String CURR_ROUND_ID = cursor.getString(cursor.getColumnIndex(KEY_CURR_ROUND_ID));
                    records.setCurrent_round_id(!TextUtils.isEmpty(CURR_ROUND_ID) ? Long.parseLong(CURR_ROUND_ID) : null);
                    String CURR_STAGE_ID = cursor.getString(cursor.getColumnIndex(KEY_CURR_STAGE_ID));
                    records.setCurrent_stage_id(!TextUtils.isEmpty(CURR_STAGE_ID) ? Long.parseLong(CURR_STAGE_ID) : null);
                    String LIVE_STANDINGS = cursor.getString(cursor.getColumnIndex(KEY_LIVE_STANDINGS));
                    records.setLive_standings(!TextUtils.isEmpty(LIVE_STANDINGS) ? Boolean.parseBoolean(LIVE_STANDINGS) : null);

                    Gson gson = new Gson();
                    Country country = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)), Country.class);
                    records.setCountry(country);

                    // Adding records to list
                    leagueList.add(records);
                }
                while (cursor.moveToNext());
            }
            db.close();
            printLog("getAllLeagueList size : " + leagueList.size());
            return leagueList;
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On getAllLeagueList data " + exc.toString());
            return null;
        }
    }

    //TODO Get all League data
    public LeagueData getLeagueDataByLeagueId(String league_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.query(TABLE_LEAGUE, new String[]{KEY_COUNTRY}, KEY_LEAGUE_ID + " = ?",
                    new String[]{league_id}, null, null, null, null);
            LeagueData records = null;
            // looping through all rows and adding to list
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    records = new LeagueData();
                    Gson gson = new Gson();
                    Country country = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)), Country.class);
                    records.setCountry(country);
                }
            }
            db.close();
            printLog("getLeagueData");
            return records;
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On getAllLeagueList data " + exc.toString());
            return null;
        }
    }

    //TODO Insert or Update Suggested Team data
    public void addUpdateSuggestedTeamList(List<SelectedTeam> teamList) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < teamList.size(); i++) {
                SelectedTeam records = teamList.get(i);
                ContentValues values = new ContentValues();
                values.put(KEY_TEAM_ID, records.getId() != null ? records.getId() + "" : "");
                values.put(KEY_LEGACY_ID, records.getLegacy_id() != null ? records.getLegacy_id() + "" : "");
                values.put(KEY_NAME, records.getName() != null ? records.getName() + "" : "");
                values.put(KEY_SHORT_CODE, records.getShort_code() != null ? records.getShort_code() + "" : "");
                values.put(KEY_TWITTER, records.getTwitter() != null ? records.getTwitter() + "" : "");
                values.put(KEY_COUNTRY_ID, records.getCountry_id() != null ? records.getCountry_id() + "" : "");
                values.put(KEY_NATIONAL_TEAM, records.getNational_team() != null ? records.getNational_team() + "" : "");
                values.put(KEY_FOUNDED, records.getFounded() != null ? records.getFounded() + "" : "");
                values.put(KEY_LOGO_PATH, records.getLogo_path() != null ? records.getLogo_path() + "" : "");
                values.put(KEY_VENUE_ID, records.getVenue_id() != null ? records.getVenue_id() + "" : "");
                values.put(KEY_PRIORITY, records.getPriority());
                values.put(KEY_NEWS_TEAM_ID, records.getNews_team_id() != null ? records.getNews_team_id() + "" : "-1");

                Cursor cursor = db.query(TABLE_SUGGESTED_TEAM, null, KEY_TEAM_ID + " = ?",
                        new String[]{String.valueOf(records.getId())}, null, null, null, null);

                int total = cursor.getCount();
                if (total > 0) {
                    // Updating Row
                    db.update(TABLE_SUGGESTED_TEAM, values, KEY_TEAM_ID + " = ? ", new String[]{String.valueOf(records.getId())});
                    printLog("UPDATE SuggestedTeam " + (i + 1) + " with Id : " + records.getId() + " => " + records.getName() + " - " + records.getName());

                } else {
                    // Inserting Row
                    db.insert(TABLE_SUGGESTED_TEAM, null, values);
                    printLog("INSERT SuggestedTeam " + (i + 1) + " with Id : " + records.getId() + " => " + records.getName() + " - " + records.getName());
                }
            }
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On SuggestedTeam Data INSERT UPDATE Query. " + exc.toString());
        }
    }

    //TODO Get SelectedTeam data
    public SelectedTeam getSuggestedTeamDataDataByTeamId(String team_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.query(TABLE_SUGGESTED_TEAM, null, KEY_TEAM_ID + " = ?",
                    new String[]{team_id}, null, null, null, null);
            SelectedTeam records = new SelectedTeam();
            // looping through all rows and adding to list
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String TEAM_ID = cursor.getString(cursor.getColumnIndex(KEY_TEAM_ID));
                    String LEGACY_ID = cursor.getString(cursor.getColumnIndex(KEY_LEGACY_ID));
                    String NAME = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                    String SHORT_CODE = cursor.getString(cursor.getColumnIndex(KEY_SHORT_CODE));
                    String TWITTER = cursor.getString(cursor.getColumnIndex(KEY_TWITTER));
                    String COUNTRY_ID = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_ID));
                    String NATIONAL_TEAM = cursor.getString(cursor.getColumnIndex(KEY_NATIONAL_TEAM));
                    String FOUNDED = cursor.getString(cursor.getColumnIndex(KEY_FOUNDED));
                    String LOGO_PATH = cursor.getString(cursor.getColumnIndex(KEY_LOGO_PATH));
                    String VENUE_ID = cursor.getString(cursor.getColumnIndex(KEY_VENUE_ID));
                    String PRIORITY = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
                    String NEWS_TEAM_ID = cursor.getString(cursor.getColumnIndex(KEY_NEWS_TEAM_ID));

                    records.setId(!TextUtils.isEmpty(TEAM_ID) ? Long.parseLong(TEAM_ID) : null);
                    records.setLegacy_id(!TextUtils.isEmpty(LEGACY_ID) ? Long.parseLong(LEGACY_ID) : null);
                    records.setName(!TextUtils.isEmpty(NAME) ? NAME : null);
                    records.setShort_code(!TextUtils.isEmpty(SHORT_CODE) ? SHORT_CODE : null);
                    records.setTwitter(!TextUtils.isEmpty(TWITTER) ? TWITTER : null);
                    records.setCountry_id(!TextUtils.isEmpty(COUNTRY_ID) ? Long.parseLong(COUNTRY_ID) : null);
                    records.setNational_team(!TextUtils.isEmpty(NATIONAL_TEAM) ? Boolean.parseBoolean(NATIONAL_TEAM) : null);
                    records.setFounded(!TextUtils.isEmpty(FOUNDED) ? Long.parseLong(FOUNDED) : null);
                    records.setLogo_path(!TextUtils.isEmpty(LOGO_PATH) ? LOGO_PATH : null);
                    records.setVenue_id(!TextUtils.isEmpty(VENUE_ID) ? Long.parseLong(VENUE_ID) : null);
                    records.setPriority(!TextUtils.isEmpty(PRIORITY) ? Integer.parseInt(PRIORITY) : null);
                    records.setNews_team_id(!TextUtils.isEmpty(NEWS_TEAM_ID) ? Long.parseLong(NEWS_TEAM_ID) : null);
                }
            }
            db.close();
            printLog("getSuggestedTeamDataDataByTeamId");
            return records;
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On getSuggestedTeamDataDataByTeamId data " + exc.toString());
            return null;
        }
    }

    //TODO Get all Player Image by playerId
    public String getPlayerImageByPlayerId(String player_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.query(TABLE_PLAYER_PHOTO, null, KEY_PLAYER_ID + " = ?",
                    new String[]{player_id}, null, null, null, null);
            String playerImagePath = null;
            // looping through all rows and adding to list
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    playerImagePath = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_IMAGE));
                }
            }
            db.close();
            printLog("getPlayerImageByPlayerId player image path with player Id : " + player_id + " => " + playerImagePath);
            return playerImagePath;
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On playerImagePath data " + exc.toString());
            return null;
        }
    }

    //TODO Insert or Update Player image data
    public void addUpdatePlayerImage(String playerId, String playerImage) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PLAYER_ID, playerId);
            values.put(KEY_PLAYER_IMAGE, playerImage);

            Cursor cursor = db.query(TABLE_PLAYER_PHOTO, new String[]{KEY_PLAYER_ID}, KEY_PLAYER_ID + " = ?",
                    new String[]{String.valueOf(playerId)}, null, null, null, null);

            int total = cursor.getCount();
            if (total > 0) {
                // Updating Row
                db.update(TABLE_PLAYER_PHOTO, values, KEY_PLAYER_ID + " = ? ", new String[]{playerId});
                printLog("UPDATE player image path with player Id : " + playerId + " => " + playerImage);

            } else {
                // Inserting Row
                db.insert(TABLE_PLAYER_PHOTO, null, values);
                printLog("INSERT player image path with player Id : " + playerId + " => " + playerImage);

            }
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On League Data INSERT UPDATE Query. " + exc.toString());
        }
    }

    // Adds a row of result to the database.
    public void addMatchVoteList(String matchID) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_VOTE_MATCH_ID, matchID);

            db.insert(TABLE_VOTE, null, values);
            db.close();
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On League Data INSERT UPDATE Query. " + exc.toString());
        }
    }

    public boolean getMatchVoteList(String matchId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_VOTE, new String[]{KEY_VOTE_MATCH_ID}, KEY_VOTE_MATCH_ID + "=?", new String[]{matchId}, null, null, null);
            cursor.moveToFirst();
            int rowCount = cursor.getCount();
            db.close();
            return rowCount > 0 ? true : false;
        } catch (Exception exc) {
            db.close();
            printLog("EXCEPTION On League Data INSERT UPDATE Query. " + exc.toString());
        }
        return false;
    }*/
}