package com.example.dartswithfriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ScoreBoardLvAdapter extends BaseAdapter {
    private List<Match> matches = new ArrayList<>();
    private int layoutId;
    private LayoutInflater inflater;

    public ScoreBoardLvAdapter(Context ctx, int layoutId, List<Match> matches) {
        this.matches = matches;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int i) {
        return matches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Match match = matches.get(i);
        View listItem = (view == null) ? inflater.inflate(this.layoutId, null) : view;

        Set<Player> players = match.game.keySet();


        if(match.game.size()==1)
        {
            for( Player x : players){
                //PLAYER 1
                ((TextView) listItem.findViewById(R.id.scoreboard_Player1_txtView)).setText(x.name);
                Double av = match.game.get(x);
                String avs = String.valueOf(av);
                //((TextView) listItem.findViewById(R.id.scoreboard_averageP1_txtView)).setText("Avg.: "+avs);
                //PLAYER 2
                //((TextView) listItem.findViewById(R.id.scoreboard_averageP2_txtView)).setText(" ");
                ((TextView) listItem.findViewById(R.id.scoreboard_player2_txtView)).setText(" ");
                //PLAYER 3
                //((TextView) listItem.findViewById(R.id.scoreboard_averageP3_txtView)).setText(" ");
                ((TextView) listItem.findViewById(R.id.scoreboard_player3_txtView)).setText(" ");
                //PLAYER 4
                //((TextView) listItem.findViewById(R.id.scoreboard_averageP4_txtView)).setText(" ");
                ((TextView) listItem.findViewById(R.id.scoreboard_player4_txtView)).setText(" ");
                //Bindestriche
                ((TextView) listItem.findViewById(R.id.bind1)).setText(" ");
                ((TextView) listItem.findViewById(R.id.bind2)).setText(" ");
                ((TextView) listItem.findViewById(R.id.bind3)).setText(" ");
            }
            ((TextView) listItem.findViewById(R.id.adresse_textview)).setText(match.adresse);
        }

        else if(match.game.size()==2)
        {
            int counter = 0;
            for( Player x : players){
                //PLAYER 1
                if(counter==0) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_Player1_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP1_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 2
                if(counter==1) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_player2_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP2_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 3
                if(counter==2) {
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP3_txtView)).setText(" ");
                    ((TextView) listItem.findViewById(R.id.scoreboard_player3_txtView)).setText(" ");
                }
                //PLAYER 4
                if(counter==3) {
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP4_txtView)).setText(" ");
                    ((TextView) listItem.findViewById(R.id.scoreboard_player4_txtView)).setText(" ");
                }

                ++counter;
            }
            //Bindestriche
            ((TextView) listItem.findViewById(R.id.bind2)).setText(" ");
            ((TextView) listItem.findViewById(R.id.bind3)).setText(" ");
            //Numoi andere Player
            ((TextView) listItem.findViewById(R.id.scoreboard_player4_txtView)).setText(" ");
            ((TextView) listItem.findViewById(R.id.scoreboard_player3_txtView)).setText(" ");
            //((TextView) listItem.findViewById(R.id.scoreboard_averageP4_txtView)).setText(" ");
            //((TextView) listItem.findViewById(R.id.scoreboard_averageP3_txtView)).setText(" ");
            ((TextView) listItem.findViewById(R.id.adresse_textview)).setText(match.adresse);
        }

        else if(match.game.size()==3)
        {
            int counter = 0;
            for( Player x : players){
                //PLAYER 1
                if(counter==0) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_Player1_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP1_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 2
                if(counter==1) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_player2_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP2_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 3
                if(counter==2) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_player3_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP3_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 4
                if(counter==3) {
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP4_txtView)).setText(" ");
                    ((TextView) listItem.findViewById(R.id.scoreboard_player4_txtView)).setText(" ");
                }
                ++counter;
            }
            //Bindestriche
            ((TextView) listItem.findViewById(R.id.bind3)).setText(" ");
            //Numoi andere Player
            ((TextView) listItem.findViewById(R.id.scoreboard_player4_txtView)).setText(" ");
            //((TextView) listItem.findViewById(R.id.scoreboard_averageP4_txtView)).setText(" ");
            ((TextView) listItem.findViewById(R.id.adresse_textview)).setText(match.adresse);
        }

        else if(match.game.size()==4)
        {
            int counter = 0;
            for( Player x : players){
                //PLAYER 1
                if(counter==0) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_Player1_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP1_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 2
                if(counter==1) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_player2_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP2_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 3
                if(counter==2) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_player3_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP3_txtView)).setText("Avg.: "+avs);
                }
                //PLAYER 4
                if(counter==3) {
                    ((TextView) listItem.findViewById(R.id.scoreboard_player4_txtView)).setText(x.name);
                    Double av = match.game.get(x);
                    String avs = String.valueOf(av);
                    //((TextView) listItem.findViewById(R.id.scoreboard_averageP4_txtView)).setText("Avg.: "+avs);
                }
                ++counter;
            }
            ((TextView) listItem.findViewById(R.id.adresse_textview)).setText(match.adresse);
        }
        return listItem;
    }
}
