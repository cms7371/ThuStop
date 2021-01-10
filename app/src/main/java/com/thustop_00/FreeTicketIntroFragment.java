package com.thustop_00;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtomato.android.ui.transformer.WheelViewTransformer;
import com.gtomato.android.ui.widget.CarouselView;
import com.thustop_00.databinding.FragmentFreeTicketIntroBinding;
import com.thustop_00.widgets.NotoTextView;

public class FreeTicketIntroFragment extends FragmentBase {
    private FragmentFreeTicketIntroBinding binding;
    private boolean isOnArrowAnimation = false;
    private final String TAG = "FreeTicketIntro";
    private float dp150;

    public static FreeTicketIntroFragment newInstance() {
        FreeTicketIntroFragment fragment = new FreeTicketIntroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _listener.setToolbarStyle(_listener.WHITE_BACK, "무료탑승권");
        dp150 = _listener.covertDPtoPX(150);
        binding = FragmentFreeTicketIntroBinding.inflate(inflater);
        WheelViewTransformer test = new WheelViewTransformer();
        binding.cvFreeTickets.setTransformer(test);
        //여러개 넘어가는 것 방지해주는 옵션
        binding.cvFreeTickets.setEnableFling(false);
        FreeTicketAdapter adapter = new FreeTicketAdapter(getContext());
        binding.cvFreeTickets.setAdapter(adapter);
        binding.cvFreeTickets.setOnScrollListener(new CarouselView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(CarouselView carouselView, int newState) {
                super.onScrollStateChanged(carouselView, newState);
                if (carouselView.getCurrentPosition() == 0) {
                    carouselView.smoothScrollToPosition(1);
                } else if (carouselView.getCurrentPosition() == (adapter.getItemCount() - 1)) {
                    carouselView.smoothScrollToPosition(adapter.getItemCount() - 2);
                }
            }
        });


        binding.tvUserInfomation.setText(Utils.getBoldSpan(getContext(),"로그인 후\n이용해주세요", new String[]{"로그인"}));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.cvFreeTickets.scrollToPosition(1);
    }

    private void animateArrows() {
        if (!isOnArrowAnimation) {
            isOnArrowAnimation = true;
            long duration = 150;
            binding.tvSlideGuide.animate().translationY(_listener.covertDPtoPX(-1) * 1.5f).setDuration(duration).withEndAction(() -> {
                binding.tvSlideGuide.animate().translationY(_listener.covertDPtoPX(1) * 1.5f).setDuration(duration).start();
            }).start();
            binding.ivTopArrow3.animate().setDuration(duration).alpha(1f).withEndAction(() -> {
                binding.ivTopArrow3.animate().setDuration(duration).alpha(0.3f).withEndAction(() -> {
                    binding.ivTopArrow2.animate().setDuration(duration).alpha(1f).withEndAction(() -> {
                        binding.ivTopArrow2.animate().setDuration(duration).alpha(0.3f).withEndAction(() -> {
                            binding.ivTopArrow1.animate().setDuration(duration).alpha(1f).withEndAction(() -> {
                                binding.ivTopArrow1.animate().setDuration(duration).alpha(0.3f).withEndAction(() -> {
                                    isOnArrowAnimation = false;
                                }).start();
                            }).start();
                        }).start();
                    }).start();
                }).start();
            }).start();
        }
    }

    private class FreeTicketAdapter extends CarouselView.Adapter<FreeTicketAdapter.FreeTicketHolder> {
        private Context context;

        FreeTicketAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public FreeTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_free_ticket_green, parent, false);

            return new FreeTicketHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FreeTicketHolder holder, int position) {
            if (position == 0 || position == getItemCount() - 1) {
                holder.textView.setBackground(getResources().getDrawable(R.drawable.bg_free_ticket_blank));
                holder.textView.setText("");
            } else {
                holder.textView.setText(Utils.getBoldSpan(getContext(),"1회\n무료 탑승권", new String[] {"1회"}));
                holder.textView.setOnTouchListener(((view, motionEvent) -> {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (holder.originY == 0f)
                                holder.originY = view.getY();
                            //터치 지점에서 Y 기준점까지  offset
                            holder.offsetY = view.getY() - motionEvent.getRawY();
                            animateArrows();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if ((holder.offsetY + motionEvent.getRawY()) <= (holder.originY - dp150) && holder.isTouchable) {
                                _listener.addFragment(FreeTicketRouteSelectionFragment.newInstance());
                                //활성화가 되면 OK 하고 break 없어서 ACTION_UP 으로 감
                            } else if (holder.isTouchable && ((holder.offsetY + motionEvent.getRawY()) <= holder.originY)) {
                                //offset + 움직인만큼 애니메이션 줘서 움직임
                                if (!binding.cvFreeTickets.isLayoutSuppressed())
                                    binding.cvFreeTickets.suppressLayout(true);
                                if (binding.ivSlideRipple.getVisibility() == View.INVISIBLE) {
                                    binding.ivSlideRipple.setVisibility(View.VISIBLE);
                                }
                                view.animate().y(motionEvent.getRawY() + holder.offsetY).setDuration(0).start();
                                float rippleScale = 1f + 4f * (holder.originY -  (motionEvent.getRawY() + holder.offsetY)) / dp150;
                                binding.ivSlideRipple.animate().scaleX(rippleScale).scaleY(rippleScale).setDuration(0).start();
                                break;
                            } else break;

                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            view.performClick();
                            if (holder.isTouchable){
                                binding.cvFreeTickets.suppressLayout(false);
                                holder.isTouchable = false;
                                view.animate().y(holder.originY).withEndAction(() -> {
                                    holder.isTouchable = true;
                                }).setDuration(500).start();
                                binding.ivSlideRipple.animate().scaleX(1f).scaleY(1f).setDuration(500).withEndAction(()->{
                                    binding.ivSlideRipple.setVisibility(View.INVISIBLE);
                                }).start();
                            }
                            break;
                    }
                    return true;
                }));
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        private class FreeTicketHolder extends RecyclerView.ViewHolder {
            public NotoTextView textView;
            public boolean isTouchable;
            public float offsetY;
            public float originY;

            public FreeTicketHolder(@NonNull View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(R.id.tv_item_free_ticket);
                isTouchable = true;
                offsetY = 0f;
                originY = 0f;
            }
        }
    }
}
