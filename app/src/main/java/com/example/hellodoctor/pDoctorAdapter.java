package com.example.hellodoctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class pDoctorAdapter extends RecyclerView.Adapter<pDoctorAdapter.pDoctorViewHolder> {

    private final Context context;
    private List<pDoctor> doctorList; // Note: List is not final anymore to allow updates

    public pDoctorAdapter(Context context, List<pDoctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public pDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pitem_doctor, parent, false);
        return new pDoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pDoctorViewHolder holder, int position) {
        pDoctor doctor = doctorList.get(position);
        holder.doctorNameTextView.setText(doctor.getName());
        holder.did.setText(doctor.getId());
        holder.specializationTextView.setText(doctor.getSpecialty());
        holder.phoneNumberTextView.setText(doctor.getPhone());

        holder.appointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, pDoctorAppointment.class);
            intent.putExtra("doctorId", doctor.getId());
            intent.putExtra("doctorName", doctor.getName());
            intent.putExtra("doctorSpecialization", doctor.getSpecialty());
            intent.putExtra("doctorPhone", doctor.getPhone());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    // Method to update the list of doctors
    public void updateList(List<pDoctor> newDoctorList) {
        this.doctorList = newDoctorList;
        notifyDataSetChanged(); // Notify the adapter to refresh the UI
    }

    public static class pDoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorNameTextView, specializationTextView, did, phoneNumberTextView;
        ImageButton appointmentButton;

        public pDoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            specializationTextView = itemView.findViewById(R.id.specializationTextView);
            did = itemView.findViewById(R.id.did);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            appointmentButton = itemView.findViewById(R.id.appointmentButton);
        }
    }
}