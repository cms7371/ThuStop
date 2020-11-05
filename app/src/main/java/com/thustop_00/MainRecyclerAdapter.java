package com.thustop_00;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.model.Route;
import com.thustop_00.model.Ticket;

import java.util.ArrayList;
import java.util.List;

//Adapter for recyclerview in main fragment. There are 3 kinds of items, button for new route,
//title to display current location, route for bus operation information
public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //It stores routes data to be displayed.
    private List<Route> data;
    private Context context;
    //Parameters represent type of item
    private static final int VIEW_TYPE_BUTTON = 0;
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_ROUTE= 2;

    //Interface for external listener for recyclerview
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
    private OnListItemSelectedInterface mListener;

    //Adapter receives list of Routes and listener from target fragment
    MainRecyclerAdapter(Context context, List<Route> in, OnListItemSelectedInterface listener) {
        this.context = context;
        this.data = in;
        this.mListener = listener;
    }



    //Method that indicates type of item depending on position.
    //We can add condition depending on date too
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_BUTTON;
        } else if (position == 1) {
            return VIEW_TYPE_TITLE;
        } else {
            return VIEW_TYPE_ROUTE;
        }
    }

    //This method gives layout of item according to the viewtype value
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BUTTON) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_route, parent, false);
            return new ButtonViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_TITLE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
            return new RouteViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }


    //It returns number of items according to input data
    @Override
    public int getItemCount() {
        if (data == null) {
            return 5;
        } else {
            return data.size() + 1;
        }
    }

    //The 3 classes fo view holder link their views with local variables.
    //Plus, refine the interface for listener
    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_route_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });

        }
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
       public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   mListener.onItemSelected(view, getAdapterPosition());
               }
           });
        }
    }

    private static class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int VIEW_TYPE_BUTTON = 0;
        private static final int VIEW_TYPE_TICKET = 1;
        private ArrayList<Ticket> tickets;
        private OnTicketSelectedInterface listener;

        TicketAdapter(ArrayList<Ticket> in, OnTicketSelectedInterface listener){
            this.tickets = in;
            this.listener = listener;

        }

        public interface OnTicketSelectedInterface {
            void onItemSelected(View v, int position);
        }

        @Override
        public int getItemViewType(int position) {
            if (tickets == null){
                return VIEW_TYPE_BUTTON;
            } else {
                if (position == (tickets.size() - 1)){
                    return VIEW_TYPE_BUTTON;
                }
                else {
                    return VIEW_TYPE_TICKET;
                }
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_BUTTON){
                //Parent가 recycler라서 에러 발생할 확률 높음
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_route, parent, false);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ButtonViewHolder extends RecyclerView.ViewHolder {
            public ButtonViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemSelected(view, getAdapterPosition());
                    }
                });
            }
        }
    }

}
