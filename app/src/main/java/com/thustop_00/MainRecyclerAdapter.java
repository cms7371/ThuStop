package com.thustop_00;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
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
    private static final int VIEW_TYPE_TICKET = 1;
    private static final int VIEW_TYPE_TITLE = 2;
    private static final int VIEW_TYPE_ROUTE = 3;

    //Interface for external listener for recyclerview
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position, int ticket_position);
    }

    private OnListItemSelectedInterface mListener;

    //Adapter receives list of Routes and listener from target fragment
    MainRecyclerAdapter(Context context, List<Route> in, OnListItemSelectedInterface listener) {
        this.context = context;
        this.data = in;
        this.mListener = listener;
        //TODO 티켓리스트와 현재 위치 이름 받아서 제목과 리사이클러 내용 바꿔줘야함
    }


    //Method that indicates type of item depending on position.
    //We can add condition depending on date too
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TICKET; //TODO 티켓 class에 따라 버튼을 만들어야 할 수도 있음
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_route, parent, false);
            return new ButtonViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_TICKET) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_ticket_holder, parent, false);
            return new TicketRecyclerHolder(itemView);
        } else if (viewType == VIEW_TYPE_TITLE) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_route, parent, false);
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
            return 5; //TODO 이 값은 테스트를 위한 값임 route 클래스 넣고 나중에 지울 수 있도록 합니다.
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
                    mListener.onItemSelected(view, getAdapterPosition(), -1);
                }
            });
        }
    }

    public class TicketRecyclerHolder extends RecyclerView.ViewHolder {
        protected RecyclerView rv;
        protected TicketAdapter adapter;

        public TicketRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.rv = itemView.findViewById(R.id.rv_tickets);
            this.adapter = new TicketAdapter(null); //TODO null말고 ticket array 넣을 것!
            this.rv.setAdapter(this.adapter);
            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(this.rv);
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
                    mListener.onItemSelected(view, getAdapterPosition(), -1);
                }
            });

        }
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO view 의 이름을 자리에 맞는 클래스에 따라 바꿔주기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition(), -1);
                }
            });
        }
    }

    private class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
        private ArrayList<Ticket> tickets;

        TicketAdapter(ArrayList<Ticket> in) {
            this.tickets = in;
        }

        @NonNull
        @Override
        public TicketAdapter.TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
            return new TicketViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TicketAdapter.TicketViewHolder holder, int position) {
            //TODO 위치에 따라 ticket 클래스 값 할당, 리스너 등록
        }

        private class TicketViewHolder extends RecyclerView.ViewHolder {
            public TicketViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemSelected(view, 0, getAdapterPosition());
                    }
                });
                //TODO 아이템들 로컬 변수로 묶어줄 것
            }
        }

        @Override
        public int getItemCount() {
            return 3; //TODO 티켓 갯수에 따라 길이 바꿔주도록 해야합니다.
        }

    }

}
