package com.example.hellodoctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class pAppointmentAdapter extends RecyclerView.Adapter<pAppointmentAdapter.AppointmentViewHolder> {

    private List<pAppointment> appointmentList;
    private Context context;
    private SelectedTimeSlotCallback callback;
    private TextView selectedTextView;
    private Set<String> unavailableSlots; // Set to track unavailable slots

    public interface SelectedTimeSlotCallback {
        void onTimeSlotSelected(String timeSlot, String date, String doctorId, String doctorName, int slotNumber);
    }

    public pAppointmentAdapter(List<pAppointment> appointmentList, Context context, SelectedTimeSlotCallback callback) {
        this.appointmentList = appointmentList;
        this.context = context;
        this.callback = callback;
        this.unavailableSlots = new HashSet<>(); // Initialize the set
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pitem_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        pAppointment appointment = appointmentList.get(position);
        holder.dateTextView.setText(appointment.getDate());
        holder.startTimeTextView.setText(appointment.getStart_time());
        holder.endTimeTextView.setText(appointment.getEnd_time());

        holder.timeSlotsContainer.removeAllViews();
        generateTimeSlots(holder.timeSlotsContainer, appointment.getStart_time(), appointment.getEnd_time(), appointment.getDate(), appointment.getDoctor_id(), appointment.getName());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, startTimeTextView, endTimeTextView;
        LinearLayout timeSlotsContainer;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            timeSlotsContainer = itemView.findViewById(R.id.timeSlotsContainer);
        }
    }

    private void generateTimeSlots(LinearLayout timeSlotsContainer, String startTime, String endTime, String date, String doctorId, String doctorName) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        try {
            startCal.setTime(sdf.parse(startTime));
            endCal.setTime(sdf.parse(endTime));
            int slotNumber = 1;

            while (startCal.before(endCal)) {
                Calendar endSlotCal = (Calendar) startCal.clone();
                endSlotCal.add(Calendar.MINUTE, 15);

                if (endSlotCal.after(endCal)) {
                    endSlotCal = endCal;
                }

                final String slot = sdf.format(startCal.getTime()) + " - " + sdf.format(endSlotCal.getTime());

                TextView timeSlotTextView = new TextView(timeSlotsContainer.getContext());
                timeSlotTextView.setText(slotNumber + ". " + slot);
                timeSlotTextView.setTextSize(14);
                timeSlotTextView.setTextColor(timeSlotsContainer.getContext().getResources().getColor(android.R.color.black));
                timeSlotTextView.setBackgroundResource(R.drawable.time_slot_bg);
                timeSlotTextView.setPadding(16, 16, 16, 16);
                timeSlotTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);

                // Check if the slot is unavailable
                if (unavailableSlots.contains(slot)) {
                    timeSlotTextView.setBackgroundResource(R.drawable.time_slot_bg_unavailable);
                    timeSlotTextView.setClickable(false);
                    timeSlotTextView.setEnabled(false);
                } else {
                    timeSlotTextView.setBackgroundResource(R.drawable.time_slot_bg);
                    timeSlotTextView.setClickable(true);
                    timeSlotTextView.setEnabled(true);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 8, 0, 8);
                timeSlotTextView.setLayoutParams(params);

                int finalSlotNumber = slotNumber;
                timeSlotTextView.setOnClickListener(v -> {
                    if (selectedTextView != null) {
                        selectedTextView.setBackgroundResource(R.drawable.time_slot_bg);
                    }
                    timeSlotTextView.setBackgroundResource(R.drawable.time_slot_bg_selected);
                    selectedTextView = timeSlotTextView;

                    callback.onTimeSlotSelected(slot, date, doctorId, doctorName, finalSlotNumber);
                });

                timeSlotsContainer.addView(timeSlotTextView);
                startCal.add(Calendar.MINUTE, 15);
                slotNumber++;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setUnavailableSlots(Set<String> unavailableSlots) {
        this.unavailableSlots = unavailableSlots;
        notifyDataSetChanged(); // Refresh the RecyclerView
    }
}