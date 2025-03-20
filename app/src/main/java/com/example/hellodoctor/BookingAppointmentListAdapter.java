package com.example.hellodoctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookingAppointmentListAdapter extends ArrayAdapter<PatientAppointment> {

    private Context context;
    private List<PatientAppointment> appointments;
    private DatabaseReference patientAppointmentsRef;

    public BookingAppointmentListAdapter(Context context, List<PatientAppointment> appointments) {
        super(context, 0, appointments);
        this.context = context;
        this.appointments = appointments;
        this.patientAppointmentsRef = FirebaseDatabase.getInstance().getReference("patient_appointments");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.booking_item_appointment, parent, false);
        }

        PatientAppointment appointment = getItem(position);

        TextView doctorNameTextView = convertView.findViewById(R.id.doctorNameTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView timeSlotTextView = convertView.findViewById(R.id.timeSlotTextView);
        TextView slotNumberTextView = convertView.findViewById(R.id.slotNumberTextView);
        Button cancelBookingButton = convertView.findViewById(R.id.cancelBookingButton);

        if (appointment != null) {
            doctorNameTextView.setText(appointment.getDoctorName());
            dateTextView.setText(appointment.getDate());
            timeSlotTextView.setText(appointment.getTimeSlot());
            slotNumberTextView.setText(String.valueOf(appointment.getSlotNumber()));

            // Set the click listener for the cancel button
            cancelBookingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showConfirmationDialog(appointment);
                }
            });
        }

        return convertView;
    }

    private void showConfirmationDialog(PatientAppointment appointment) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> cancelBooking(appointment))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelBooking(PatientAppointment appointment) {
        if (appointment == null) {
            Toast.makeText(context, "Error: Appointment not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove the appointment from Firebase
        patientAppointmentsRef.child(appointment.getId()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Notify the user
                        Toast.makeText(context, "Booking cancelled successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, remove the appointment from the list
                        appointments.remove(appointment);
                        notifyDataSetChanged();
                    } else {
                        // Notify the user of the failure
                        Toast.makeText(context, "Failed to cancel booking: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}