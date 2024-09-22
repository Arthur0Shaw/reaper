"use client"; // Ensure this is a client-side component

import React, { useState, useEffect, useCallback } from "react";
import {
  Grid,
  Box,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Typography,
  CircularProgress,
} from "@mui/material";
import { LocalizationProvider, DatePicker } from "@mui/x-date-pickers";
import { AdapterMoment } from "@mui/x-date-pickers/AdapterMoment";
import moment from "moment";
import axios from "axios";
import ReportTable from "../components/dashboard/ReportTable"; // Ensure correct path to ReportTable
import { IconFilter, IconCalendar, IconReportSearch, IconMoodSad } from "@tabler/icons-react"; // Icons for no data, filters, etc.

const Reports = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // State for filters
  const [selectedDateFrom, setSelectedDateFrom] = useState(moment());
  const [selectedDateTo, setSelectedDateTo] = useState(moment());
  const [status, setStatus] = useState("");
  const [id, setId] = useState("");
  const [acquirerReferenceId, setAcquirerReferenceId] = useState("");
  const [merchantOrderId, setMerchantOrderId] = useState("");

  // Define status options
  const statusOptions = ["FAILURE", "SUCCESS", "PENDING"];

  // Debounce timer state
  const [debounceTimer, setDebounceTimer] = useState<NodeJS.Timeout | null>(null);

  // Fetch report data from API
  const fetchReportData = useCallback(async () => {
    try {
      setLoading(true);

      const token = localStorage.getItem("token"); // Ensure token is present
      if (!token) {
        throw new Error("Authorization token not found");
      }

      const payload = {
        dateIndexFrom: selectedDateFrom.format("YYYYMMDD"),
        dateIndexTo: selectedDateTo.format("YYYYMMDD"),
        status: status || null,
        id: id || null,
        acquirerReferenceId: acquirerReferenceId || null,
        merchantOrderId: merchantOrderId || null,
      };

      const response = await axios.post(
        "http://localhost:8080/api/v1/transaction/report",
        payload,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const data = response.data.transactions;
      setTransactions(data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching report data:", error);
      setError(error.message);
      setLoading(false);
    }
  }, [selectedDateFrom, selectedDateTo, status, id, acquirerReferenceId, merchantOrderId]);

  // Trigger API call when any filter is updated
  useEffect(() => {
    fetchReportData();
  }, [selectedDateFrom, selectedDateTo, status, id, acquirerReferenceId, merchantOrderId, fetchReportData]);

  // Handle input changes with debounce for API call
  const handleDebouncedInputChange = (setter: (value: string) => void) => (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setter(value);

    if (debounceTimer) {
      clearTimeout(debounceTimer);
    }

    setDebounceTimer(setTimeout(fetchReportData, 2000));
  };

  const handleDateFromChange = (newDate: any) => setSelectedDateFrom(newDate);
  const handleDateToChange = (newDate: any) => setSelectedDateTo(newDate);
  const handleStatusChange = (e: React.ChangeEvent<{ value: unknown }>) => setStatus(e.target.value as string);

  return (
    <Box p={4} sx={{ backgroundColor: "#f0f4f8", minHeight: "100vh" }}>
      {/* Filters */}
      <Box
        display="flex"
        gap={3}
        mb={4}
        flexWrap="wrap"
        justifyContent="center"
        sx={{
          backgroundColor: "#fff",
          padding: "2rem",
          borderRadius: "16px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.05)",
        }}
      >
        <LocalizationProvider dateAdapter={AdapterMoment}>
          <DatePicker
            label="From Date"
            value={selectedDateFrom}
            onChange={handleDateFromChange}
            renderInput={(params) => (
              <TextField
                {...params}
                sx={{
                  backgroundColor: "#ffffff",
                  borderRadius: "8px",
                  "& .MuiOutlinedInput-root": {
                    borderRadius: "8px",
                  },
                }}
                InputProps={{
                  startAdornment: (
                    <IconCalendar
                      size={18}
                      style={{ marginRight: "8px", color: "#888" }}
                    />
                  ),
                }}
              />
            )}
          />
          <DatePicker
            label="To Date"
            value={selectedDateTo}
            onChange={handleDateToChange}
            renderInput={(params) => (
              <TextField
                {...params}
                sx={{
                  backgroundColor: "#ffffff",
                  borderRadius: "8px",
                  "& .MuiOutlinedInput-root": {
                    borderRadius: "8px",
                  },
                }}
                InputProps={{
                  startAdornment: (
                    <IconCalendar
                      size={18}
                      style={{ marginRight: "8px", color: "#888" }}
                    />
                  ),
                }}
              />
            )}
          />
        </LocalizationProvider>

        <FormControl
          sx={{
            minWidth: 180,
            backgroundColor: "#ffffff",
            borderRadius: "8px",
            "& .MuiOutlinedInput-root": {
              borderRadius: "8px",
            },
          }}
        >
          <InputLabel>Status</InputLabel>
          <Select value={status} label="Status" onChange={handleStatusChange}>
            <MenuItem value="">
              <em>None</em>
            </MenuItem>
            {statusOptions.map((option) => (
              <MenuItem key={option} value={option}>
                {option}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <TextField
          label="Transaction ID"
          value={id}
          onChange={handleDebouncedInputChange(setId)}
          sx={{
            backgroundColor: "#ffffff",
            borderRadius: "8px",
            "& .MuiOutlinedInput-root": {
              borderRadius: "8px",
            },
          }}
          InputProps={{
            startAdornment: (
              <IconReportSearch
                size={18}
                style={{ marginRight: "8px", color: "#888" }}
              />
            ),
          }}
        />
        <TextField
          label="Acquirer Reference ID"
          value={acquirerReferenceId}
          onChange={handleDebouncedInputChange(setAcquirerReferenceId)}
          sx={{
            backgroundColor: "#ffffff",
            borderRadius: "8px",
            "& .MuiOutlinedInput-root": {
              borderRadius: "8px",
            },
          }}
          InputProps={{
            startAdornment: (
              <IconReportSearch
                size={18}
                style={{ marginRight: "8px", color: "#888" }}
              />
            ),
          }}
        />
        <TextField
          label="Merchant Order ID"
          value={merchantOrderId}
          onChange={handleDebouncedInputChange(setMerchantOrderId)}
          sx={{
            backgroundColor: "#ffffff",
            borderRadius: "8px",
            "& .MuiOutlinedInput-root": {
              borderRadius: "8px",
            },
          }}
          InputProps={{
            startAdornment: (
              <IconReportSearch
                size={18}
                style={{ marginRight: "8px", color: "#888" }}
              />
            ),
          }}
        />
      </Box>

      {/* Display loading, error, or data */}
      {loading ? (
        <Box display="flex" justifyContent="center" alignItems="center" height="100px">
          <CircularProgress />
        </Box>
      ) : error ? (
        <Typography color="error">{error}</Typography>
      ) : transactions.length === 0 ? (
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          flexDirection="column"
          height="300px"
          sx={{
            backgroundColor: "#e3f2fd",
            borderRadius: "12px",
            padding: "2rem",
            textAlign: "center",
          }}
        >
          <IconMoodSad size={48} color="#90a4ae" />
          <Typography variant="h6" color="textSecondary" sx={{ mt: 2 }}>
            No transactions found for the selected filters.
          </Typography>
        </Box>
      ) : (
        <Box
          // sx={{
          //   backgroundColor: "#fff",
          //   borderRadius: "12px",
          //   padding: "1.5rem",
          //   boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          // }}
        >
          {/* <Typography
            variant="h6"
            sx={{
              mb: 2,
              fontWeight: "bold",
              color: "#1565c0",
              textAlign: "center",
            }}
          >
            Transaction Report
          </Typography> */}
          <ReportTable transactions={transactions} /> {/* Render the table */}
        </Box>
      )}
    </Box>
  );
};

export default Reports;
