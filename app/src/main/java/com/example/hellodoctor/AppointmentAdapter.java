package com.example.hellodoctor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.tvDoctorID.setText(appointment.getDoctor_id());
        holder.tvDate.setText(appointment.getDate());
        holder.tvStartTime.setText(appointment.getStart_time());
        holder.tvEndTime.setText(appointment.getEnd_time());
        holder.tvNumPatients.setText(appointment.getNum_patients());
        holder.tvNumReports.setText(appointment.getNum_reports());
        holder.tvTimeID.setText(appointment.getTime_id());

        holder.btnAnyChanges.setOnClickListener(v -> showUpdateDialog(appointment));
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(appointment, position));
        holder.btnPatientNo.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdatePatientNo.class);
            intent.putExtra("DOCTOR_ID", appointment.getDoctor_id());
            intent.putExtra("TIME_ID", appointment.getTime_id());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    private void showUpdateDialog(Appointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_update_appointment, null);
        builder.setView(view);

        EditText etStartTime = view.findViewById(R.id.etStartTime);
        EditText etEndTime = view.findViewById(R.id.etEndTime);
        EditText etNumPatients = view.findViewById(R.id.etNumPatients);
        EditText etNumReports = view.findViewById(R.id.etNumReports);

        etStartTime.setText(appointment.getStart_time());
        etEndTime.setText(appointment.getEnd_time());
        etNumPatients.setText(appointment.getNum_patients());
        etNumReports.setText(appointment.getNum_reports());

        builder.setTitle("Update Appointment Details")
                .setPositiveButton("Update", (dialog, which) -> {
                    String startTime = etStartTime.getText().toString();
                    String endTime = etEndTime.getText().toString();
                    String numPatients = etNumPatients.getText().toString();
                    String numReports = etNumReports.getText().toString();

                    if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)
                            && !TextUtils.isEmpty(numPatients) && !TextUtils.isEmpty(numReports)) {
                        updateAppointment(appointment.getTime_id(), startTime, endTime, numPatients, numReports);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateAppointment(String timeId, String startTime, String endTime, String numPatients, String numReports) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("doctor_time").child(timeId);
        dbRef.child("start_time").setValue(startTime);
        dbRef.child("end_time").setValue(endTime);
        dbRef.child("num_patients").setValue(numPatients);
        dbRef.child("num_reports").setValue(numReports);
    }

    private void showDeleteConfirmationDialog(Appointment appointment, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Appointment");
        builder.setMessage("Are you sure you want to delete this appointment?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteAppointment(appointment, position));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteAppointment(Appointment appointment, int position) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("doctor_time").child(appointment.getTime_id());
        dbRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                appointmentList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, appointmentList.size());

                // Restart the activity to refresh the data
                if (context instanceof ViewAllRecordsActivity) {
                    ((ViewAllRecordsActivity) context).recreate();
                }
            }
        });
    }


    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDoctorID, tvDate, tvStartTime, tvEndTime, tvNumPatients, tvNumReports, tvTimeID;
        public Button btnAnyChanges, btnDelete, btnPatientNo;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorID = itemView.findViewById(R.id.tvDoctorID);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvNumPatients = itemView.findViewById(R.id.tvNumPatients);
            tvNumReports = itemView.findViewById(R.id.tvNumReports);
            tvTimeID = itemView.findViewById(R.id.tvTimeID);
            btnAnyChanges = itemView.findViewById(R.id.btnAnyChanges);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnPatientNo = itemView.findViewById(R.id.btnPatientNo);
        }
    }
}