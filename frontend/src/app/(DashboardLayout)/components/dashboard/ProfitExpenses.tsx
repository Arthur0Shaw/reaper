"use client"; // Ensure this is a client-side component

import React, { useEffect, useState } from "react";
import { Box, IconButton, Menu, MenuItem, TextField, Select, FormControl, InputLabel, SelectChangeEvent } from "@mui/material";
import { LocalizationProvider, DatePicker } from "@mui/x-date-pickers";
import { AdapterMoment } from "@mui/x-date-pickers/AdapterMoment";
import DashboardCard from "@/app/(DashboardLayout)/components/shared/DashboardCard";
import dynamic from "next/dynamic";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import { useTheme } from "@mui/material/styles";
import moment, { Moment } from "moment";
import axios from "axios";
import { ApexOptions } from "apexcharts"; // Import ApexOptions

const Chart = dynamic(() => import("react-apexcharts"), { ssr: false });

interface ProfitExpensesProps {
  dateWiseTransactionCount: { [key: string]: number }; // Assuming dateWiseTransactionCount is an object with string keys and number values
  dateWiseTransactionAmount: { [key: string]: number }; // Assuming dateWiseTransactionAmount is an object with string keys and number values
  onFilterChange: (data: any) => void; // Assuming onFilterChange is a function that takes data
}

const ProfitExpenses: React.FC<ProfitExpensesProps> = ({ dateWiseTransactionCount, dateWiseTransactionAmount, onFilterChange }) => {
  const [chartData, setChartData] = useState({
    categories: Object.keys(dateWiseTransactionCount || {}),
    countSeries: Object.values(dateWiseTransactionCount || {}),
    amountSeries: Object.values(dateWiseTransactionAmount || {}),
  });

  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const theme = useTheme();
  const primary = "#1f4cb6";
  const secondary = "#a9e3f4";

  // State for Date Range and Status
  const [selectedDateFrom, setSelectedDateFrom] = useState<Moment | null>(moment());
  const [selectedDateTo, setSelectedDateTo] = useState<Moment | null>(moment());
  const [status, setStatus] = useState("");

  // Define status options
  const statusOptions = ["FAILURE", "SUCCESS", "PENDING"];

  const fetchDashboardData = async (payload: any) => {
    try {
      const token = localStorage.getItem('token'); // Assuming token is stored in localStorage
      const response = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/transaction/dashboard`, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = response.data;
      setChartData({
        categories: Object.keys(data.dateWiseTransactionCount || {}),
        countSeries: Object.values(data.dateWiseTransactionCount || {}),
        amountSeries: Object.values(data.dateWiseTransactionAmount || {}),
      });

      // Notify parent to update totals
      onFilterChange(data);
    } catch (error) {
      console.error("Error fetching dashboard data:", error);
    }
  };

  // Function to trigger API call
  const handleFilterChange = () => {
    const payload = {
      dateIndexFrom: selectedDateFrom?.format("YYYYMMDD") || "",
      dateIndexTo: selectedDateTo?.format("YYYYMMDD") || "",
      status: status || null,
    };

    // Trigger the API call when filters are updated
    fetchDashboardData(payload);
  };

  // Handle date changes for 'From' date
  const handleDateFromChange = (newDate: Moment | null) => {
    setSelectedDateFrom(newDate);
  };

  // Handle date changes for 'To' date
  const handleDateToChange = (newDate: Moment | null) => {
    setSelectedDateTo(newDate);
  };

  // Handle status change
  const handleStatusChange = (event: SelectChangeEvent<string>) => {
    setStatus(event.target.value);
  };

  // Trigger API call when date or status is updated
  useEffect(() => {
    handleFilterChange();
  }, [selectedDateFrom, selectedDateTo, status]);

  const optionscolumnchart: ApexOptions = {
    chart: {
      type: "bar", // Explicitly set the chart type
      toolbar: { show: true },
      height: 250, // Slim chart height
    },
    colors: [primary, secondary],
    xaxis: {
      categories: chartData.categories.length ? chartData.categories : ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    },
    tooltip: {
      theme: theme.palette.mode === "dark" ? "dark" : "light",
    },
  };

  const seriescolumnchart = [
    {
      name: "Transaction Count",
      data: chartData.countSeries.length ? chartData.countSeries : [0, 0, 0, 0, 0, 0, 0],
    },
    {
      name: "Transaction Amount",
      data: chartData.amountSeries.length ? chartData.amountSeries : [0, 0, 0, 0, 0, 0, 0],
    },
  ];

  return (
    <DashboardCard
      title="Transaction Analytics"
      action={
        <>
          <IconButton aria-label="more" onClick={handleClick}>
            <MoreVertIcon />
          </IconButton>
          <Menu anchorEl={anchorEl} open={open} onClose={handleClose}>
            <MenuItem onClick={handleClose}>Action</MenuItem>
          </Menu>
        </>
      }
    >
      <Box>
        <Box display="flex" alignItems="center" justifyContent="flex-start" mb={2} gap={2}>
          <LocalizationProvider dateAdapter={AdapterMoment}>
            <DatePicker
              label="From Date"
              value={selectedDateFrom}
              onChange={handleDateFromChange}
              slots={{ textField: TextField }} // Updated from renderInput to slots
            />
          </LocalizationProvider>

          <LocalizationProvider dateAdapter={AdapterMoment}>
            <DatePicker
              label="To Date"
              value={selectedDateTo}
              onChange={handleDateToChange}
              slots={{ textField: TextField }} // Updated from renderInput to slots
            />
          </LocalizationProvider>

          <FormControl sx={{ minWidth: 200 }}>
            <InputLabel>Status</InputLabel>
            <Select value={status} label="Status" onChange={handleStatusChange}>
              <MenuItem value="">
                <em>All</em>
              </MenuItem>
              {statusOptions.map((option) => (
                <MenuItem key={option} value={option}>
                  {option}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Box>

        <Box>
          <Chart options={optionscolumnchart} series={seriescolumnchart} type="bar" width={"100%"} height={500} />
        </Box>
      </Box>
    </DashboardCard>
  );
};

export default ProfitExpenses;
