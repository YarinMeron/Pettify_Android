package com.pettify.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pettify.R;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ViewReportFragment extends Fragment {
    Report report;
    TextView report_title;
    TextView report_description;
    TextView report_last_updated_on;
    TextView report_location;
    TextView reporter_phone;

    ImageView report_image;
    ReportListViewModel reportViewModel;
    AuthViewModel authViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportViewModel =
                new ViewModelProvider(this).get(ReportListViewModel.class);
        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_report, container, false);
        report_description = view.findViewById(R.id.report_description);
        report_image = view.findViewById(R.id.report_image);
        report_location = view.findViewById(R.id.report_address);
        report_last_updated_on = view.findViewById(R.id.report_last_updated_at);
        report_title = view.findViewById(R.id.report_title);
        reporter_phone = view.findViewById(R.id.reporterPhone_TextView);

        final String reportId = ViewReportFragmentArgs.fromBundle(getArguments()).getReportId();
        reportViewModel.getReport(reportId, new Listener<Report>() {
            @Override
            public void onComplete(Report data) {
                report = data;
                authViewModel.getUser(report.getReporterId(), new Listener<User>() {
                    @Override
                    public void onComplete(User user) {
                        reporter_phone.setText("Reporter Phone Number: " + user.getPhoneNumber());
                    }
                });
                report_title.setText(report.getTitle());
                report_description.setText(report.getDescription());
                report_location.setText("Location: " + report.getAddress());
                Date date = new Date(report.getLastUpdated() * 1000L);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                report_last_updated_on.setText("Last updated at: " + format.format(date));



                if (data.getImage_url() != null){
                    Picasso.get().load(data.getImage_url()).placeholder(R.drawable.images).into(report_image);
                }


            }
        });

        return view;
    }
}
