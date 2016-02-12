/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grarak.kerneladiutor.fragments.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.elements.DDivider;
import com.grarak.kerneladiutor.elements.cards.CardViewItem;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.database.CommandDB;

import java.util.List;


/**
 * Created by willi on 25.04.15.
 */
public class StartUpCommandsFragment extends RecyclerViewFragment {

    private CardViewItem.DCardView mStartUpCommandsDelete;
    private CardViewItem.DCardView[] mStartUpCommands;

    @Override
    public boolean showApplyOnBoot() {
        return false;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        listcommands();
    }

    public void listcommands () {
        CommandDB commandDB = new CommandDB(getActivity());
        List<CommandDB.CommandItem> commandItems = commandDB.getAllCommands();

        if (commandItems.size() > 0) {
            mStartUpCommandsDelete = new CardViewItem.DCardView();
            mStartUpCommandsDelete.setTitle(getString(R.string.startup_commands_delete));
            mStartUpCommandsDelete.setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
                @Override
                public void onClick(CardViewItem.DCardView dCardView) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to delete all Start-up Commands??").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
            addView(mStartUpCommandsDelete);

            DDivider mStartUpCommandsListDividerCard = new DDivider();
            mStartUpCommandsListDividerCard.setText(getString(R.string.startup_commands_list));
            addView(mStartUpCommandsListDividerCard);

            mStartUpCommands = new CardViewItem.DCardView[commandItems.size()];
            for (int i = 0; i < commandItems.size(); i++) {
                String command = commandItems.get(i).getCommand();
                mStartUpCommands[i] = new CardViewItem.DCardView();
                mStartUpCommands[i].setDescription(command);

                addView(mStartUpCommands[i]);
            }
        }
        else {
                mStartUpCommandsDelete = new CardViewItem.DCardView();
                mStartUpCommandsDelete.setTitle("No startup Commands Found");

                addView(mStartUpCommandsDelete);
        }
    }

    public void deletecommands(Context context) {
        CommandDB commandDB = new CommandDB(context);

        List<CommandDB.CommandItem> commandItems = commandDB.getAllCommands();
        for (int i = 0; i <= commandItems.size(); i++) {
                commandDB.delete(0);
        }

        commandDB.commit();
        view.invalidate();
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    deletecommands(getActivity());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}
