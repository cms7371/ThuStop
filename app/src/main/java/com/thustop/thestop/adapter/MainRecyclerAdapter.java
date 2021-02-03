package com.thustop.thestop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop.R;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;

import java.util.List;
import java.util.Locale;

//Adapter for recyclerview in main fragment. There are 3 kinds of items, button for new route,
//title to display current location, route for bus operation information
public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //It stores routes data to be displayed.
    private List<Route> routes;
    private List<Ticket> tickets;
    private Context context;
    private boolean isFreeTicket;
    private int routeOffset;
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
    public MainRecyclerAdapter(Context context, boolean isFreeTicket, List<Route> in, OnListItemSelectedInterface listener) {
        this.context = context;
        this.isFreeTicket = isFreeTicket;
        this.routes = in;
        this.mListener = listener;
        if (isFreeTicket)
            routeOffset = 1;
        else
            routeOffset = 2;
        //TODO 티켓리스트와 현재 위치 이름 받아서 제목과 리사이클러 내용 바꿔줘야함
    }


    public void updateRoutes(List<Route> data) {
        if (this.routes != null) {
            notifyItemRangeRemoved(routeOffset, this.routes.size());
        }
        this.routes = data;
        notifyItemRangeInserted(routeOffset, this.routes.size());
    }

    public void updateTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        notifyItemRemoved(0);
        notifyItemInserted(0);
    }


    //Method that indicates type of item depending on position.
    //We can add condition depending on date too
    @Override
    public int getItemViewType(int position) {
        if (isFreeTicket) {
            if (position == 0)
                return VIEW_TYPE_TITLE;
            else
                return VIEW_TYPE_ROUTE;
        } else {
            if (position == 0) {
                //TODO 티켓 class에 따라 버튼을 만들어야 할 수도 있음
                if (tickets == null)
                    return VIEW_TYPE_BUTTON;
                else
                    return VIEW_TYPE_TICKET;
            } else if (position == 1) {
                return VIEW_TYPE_TITLE;
            } else if (position == (routes.size() + 2)) {
                return VIEW_TYPE_TITLE; //TODO 이거도 사업자 정보 지워야함
            } else if (position >= 2) {
                return VIEW_TYPE_ROUTE;
            }
        }
        return 0;
    }

    //This method gives layout of item according to the viewtype value
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BUTTON) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_add_route_button, parent, false);
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
        if (holder instanceof RouteViewHolder) {
            Route cnt_route = routes.get(position - routeOffset);
            int boarding_stop_num = cnt_route.boarding_stops.size();
            int alighting_stop_num = cnt_route.alighting_stops.size();
            ((RouteViewHolder) holder).tvName.setText(cnt_route.name);
            ((RouteViewHolder) holder).tvStatus.setText(cnt_route.status);
            ((RouteViewHolder) holder).tvPeople.setText(String.format(Locale.KOREA, "%d/%d", cnt_route.cnt_passenger, cnt_route.max_passenger));
            if (cnt_route.status.equals("모집중")) {
                ((RouteViewHolder) holder).tvStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_round12_red));
            }
            //시간 반영
            ((RouteViewHolder) holder).tvDepartureTime.setText(cnt_route.boarding_stops.get(0).time);
            ((RouteViewHolder) holder).tvDestinationTime.setText(cnt_route.alighting_stops.get(alighting_stop_num - 1).time);

            ((RouteViewHolder) holder).tvDeparture1.setText(cnt_route.getBoardingStopName(0));
            if (boarding_stop_num != 2) {
                ((RouteViewHolder) holder).tvDeparture2.setText(cnt_route.getBoardingStopName(boarding_stop_num / 2));
            }
            ((RouteViewHolder) holder).tvDeparture3.setText(cnt_route.getBoardingStopName(boarding_stop_num - 1));

            ((RouteViewHolder) holder).tvDestination1.setText(cnt_route.getAlightingStopName(0));
            if (alighting_stop_num != 2) {
                ((RouteViewHolder) holder).tvDestination2.setText(cnt_route.getAlightingStopName(alighting_stop_num / 2));
            }
            ((RouteViewHolder) holder).tvDestination3.setText(cnt_route.getAlightingStopName(alighting_stop_num - 1));
        } else if (isFreeTicket && holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).title.setText("탑승 가능 노선");
            ((TitleViewHolder) holder).title.setTextColor(ContextCompat.getColor(context, R.color.TextBlack));
        } else if ((routes != null) && (position == (routes.size() + 2)) && (holder instanceof TitleViewHolder)) {
            ((TitleViewHolder) holder).layout.setBackground(ContextCompat.getDrawable(context, R.color.Primary));
            ((TitleViewHolder) holder).title.setTextColor(ContextCompat.getColor(context, R.color.White));
            ((TitleViewHolder) holder).title.setText(R.string.business_information_content);
            ((TitleViewHolder) holder).title.setTextSize(12);
            //TODO 메인화면 사업자 표시용 코드, 없애야함.
        }
    }


    //It returns number of items according to input data
    @Override
    public int getItemCount() {
        if (isFreeTicket) {
            if (routes == null)
                return 1;
            else
                return routes.size() + 1;
        } else {
            if (routes == null)
                return 2; //TODO 이 값은 테스트를 위한 값임 route 클래스 넣고 나중에 지울 수 있도록 합니다.
            else
                //TODO 마지막 사업자 정보 지워야함
                return routes.size() + 3;
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
        protected TicketRecyclerAdapter adapter;

        public TicketRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.rv = itemView.findViewById(R.id.rv_tickets);
            this.adapter = new TicketRecyclerAdapter(context, tickets, true); //TODO null말고 ticket array 넣을 것!
            adapter.setListener((view, position) -> mListener.onItemSelected(view, 0, position));
            this.rv.setAdapter(this.adapter);
            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(this.rv);
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ConstraintLayout layout;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_route_title);
            this.layout = (ConstraintLayout) itemView.findViewById(R.id.cl_route_title);

/*            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition(), -1);
                }
            });*/

        }
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvStatus;
        public TextView tvPeople;
        public TextView tvDepartureTime;
        public TextView tvDeparture1;
        public TextView tvDeparture2;
        public TextView tvDeparture3;
        public TextView tvDestinationTime;
        public TextView tvDestination1;
        public TextView tvDestination2;
        public TextView tvDestination3;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO view 의 이름을 자리에 맞는 클래스에 따라 바꿔주기
            this.tvName = itemView.findViewById(R.id.tv_mrv_code);
            this.tvStatus = itemView.findViewById(R.id.tv_mrv_state);
            this.tvPeople = itemView.findViewById(R.id.tv_mrv_num);
            this.tvDepartureTime = itemView.findViewById(R.id.tv_mrv_time_departure);
            this.tvDeparture1 = itemView.findViewById(R.id.tv_mrv_departure1);
            this.tvDeparture2 = itemView.findViewById(R.id.tv_mrv_departure2);
            this.tvDeparture3 = itemView.findViewById(R.id.tv_mrv_departure3);
            this.tvDestinationTime = itemView.findViewById(R.id.tv_mrv_time_arrive);
            this.tvDestination1 = itemView.findViewById(R.id.tv_mrv_destination1);
            this.tvDestination2 = itemView.findViewById(R.id.tv_mrv_destination2);
            this.tvDestination3 = itemView.findViewById(R.id.tv_mrv_destination3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition(), -1);
                }
            });
        }
    }

}
