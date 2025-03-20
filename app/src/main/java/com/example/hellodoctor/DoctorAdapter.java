package com.example.hellodoctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private Context context;
    private DatabaseReference databaseReference;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context; // Initialize context properly
        this.doctorList = doctorList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("doctors");
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.textViewID.setText(doctor.getId());
        holder.textViewName.setText(doctor.getName());
        holder.textViewSpecialty.setText(doctor.getSpecialty());
        holder.textViewPhone.setText(doctor.getPhone());
        holder.textViewEmail.setText(doctor.getEmail());



        holder.viewdetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, CurrentActivity.class);
            intent.putExtra("DOCTOR_ID", doctor.getId());
            intent.putExtra("DOCTOR_NAME", doctor.getName());
            intent.putExtra("DOCTOR_SPECIALTY", doctor.getSpecialty());
            intent.putExtra("DOCTOR_PHONE", doctor.getPhone());
            intent.putExtra("DOCTOR_EMAIL", doctor.getEmail());
            context.startActivity(intent);
        });



        holder.buttonDelete.setOnClickListener(v -> {
            Toast.makeText(context, "Delete button clicked for ID: " + doctor.getId(), Toast.LENGTH_SHORT).show();
            deleteDoctor(doctor.getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    private void deleteDoctor(String doctorId, int position) {
        databaseReference.child(doctorId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    doctorList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Doctor deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete doctor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView textViewID, textViewName, textViewSpecialty, textViewPhone, textViewEmail;
        Button viewdetails, buttonDelete;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.textViewID);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSpecialty = itemView.findViewById(R.id.textViewSpecialty);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            viewdetails = itemView.findViewById(R.id.viewdetails);
            buttonDelete = itemView.findViewById(R.id.btnDelete); // Ensure this button exists
        }
    }
}