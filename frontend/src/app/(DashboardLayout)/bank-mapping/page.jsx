"use client";

import React, { useState } from "react";
import {
  Box,
  Grid,
  TextField,
  Typography,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Button,
  InputAdornment,
  Paper,
  IconButton,
} from "@mui/material";
import { AccountBalance, Lock, VpnKey, Fingerprint, Add, Remove } from "@mui/icons-material"; // Icons
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const BankMappingPage = () => {
  const [selectedBank, setSelectedBank] = useState(""); // Bank dropdown state
  const [loginID, setLoginID] = useState("");
  const [password, setPassword] = useState("");
  const [adf1, setAdf1] = useState("");
  const [adf2, setAdf2] = useState("");
  const [vpaCount, setVpaCount] = useState(1); // VPA count
  const [vpas, setVpas] = useState([""]); // State for VPA inputs
  const [errors, setErrors] = useState({}); // State for form validation errors

  // Bank options
  const bankOptions = ["Yes Bank", "SBI", "HDFC", "ICICI", "BOB", "Bandhan Bank", "IDFC", "Axis Bank"];

  // Handle dropdown change
  const handleBankChange = (event) => {
    setSelectedBank(event.target.value);
    setErrors((prevErrors) => ({ ...prevErrors, selectedBank: "" }));
  };

  // Handle VPA input change
  const handleVpaChange = (index, value) => {
    const updatedVpas = [...vpas];
    updatedVpas[index] = value;
    setVpas(updatedVpas);
    setErrors((prevErrors) => ({ ...prevErrors, vpas: "" }));
  };

  // Handle adding and removing VPA inputs
  const incrementVPA = () => {
    setVpas([...vpas, ""]);
    setVpaCount(vpaCount + 1);
  };

  const decrementVPA = () => {
    if (vpaCount > 1) {
      setVpas(vpas.slice(0, -1));
      setVpaCount(vpaCount - 1);
    }
  };

  // Handle save action
  const handleSave = async () => {
    const validationErrors = {};
    if (!selectedBank) validationErrors.selectedBank = "Please select a bank.";
    if (!loginID) validationErrors.loginID = "Please enter your login ID.";
    if (!password) validationErrors.password = "Please enter your password.";
    if (!vpas.some((vpa) => vpa)) validationErrors.vpas = "Please enter at least one VPA.";

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      toast.error("Please fill out all required fields.");
      return;
    }

    const payload = {
      bank: selectedBank,
      username: loginID,
      password,
      vpa: vpas.join(","),
      adf1: adf1 || null,
      adf2: adf2 || null,
    };

    try {
      const token = localStorage.getItem("token"); // Assuming token is stored in localStorage
      const response = await axios.post("http://localhost:8080/api/v1/acquirer/saveMapping", payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200 || response.status === 201) {
        toast.success("Bank mapping saved successfully!");
      } else {
        toast.error("Failed to save bank mapping. Please try again.");
      }
    } catch (error) {
      toast.error("Error occurred. Please try again.");
      console.error("API error:", error);
    }
  };

  return (
    <Box>
      <ToastContainer /> {/* ToastContainer for toasts */}
      <Paper
        elevation={3}
        sx={{
          padding: 3,
          backgroundColor: "#ffffff",
          borderRadius: "16px",
          boxShadow: "0 4px 10px rgba(0, 0, 0, 0.05)",
          minWidth: "100vh",
        }}
      >
        <Grid container spacing={4}>
          {/* Left Side - Bank Dropdown and Inputs */}
          <Grid item xs={12} md={6}>
            <Box p={6}>
              <Typography variant="h5" gutterBottom sx={{ fontWeight: "bold", color: "#1f4cb6" }}>
                Bank and Credentials
              </Typography>

              {/* Bank Dropdown */}
              <FormControl fullWidth variant="outlined" sx={{ marginBottom: 3 }} error={!!errors.selectedBank}>
                <InputLabel>Select Bank</InputLabel>
                <Select value={selectedBank} onChange={handleBankChange} label="Select Bank">
                  {bankOptions.map((bank) => (
                    <MenuItem key={bank} value={bank}>
                      <AccountBalance sx={{ mr: 1 }} /> {bank}
                    </MenuItem>
                  ))}
                </Select>
                {errors.selectedBank && (
                  <Typography variant="caption" color="error">
                    {errors.selectedBank}
                  </Typography>
                )}
              </FormControl>

              {/* Login ID */}
              <TextField
                fullWidth
                label="Login ID"
                variant="outlined"
                value={loginID}
                onChange={(e) => {
                  setLoginID(e.target.value);
                  setErrors((prevErrors) => ({ ...prevErrors, loginID: "" }));
                }}
                error={!!errors.loginID}
                helperText={errors.loginID}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <VpnKey />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              {/* Password */}
              <TextField
                fullWidth
                label="Password"
                variant="outlined"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  setErrors((prevErrors) => ({ ...prevErrors, password: "" }));
                }}
                error={!!errors.password}
                helperText={errors.password}
                type="password"
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Lock />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              {/* ADF1 */}
              <TextField
                fullWidth
                label="ADF1"
                variant="outlined"
                value={adf1}
                onChange={(e) => setAdf1(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Fingerprint />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              {/* ADF2 */}
              <TextField
                fullWidth
                label="ADF2"
                variant="outlined"
                value={adf2}
                onChange={(e) => setAdf2(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Fingerprint />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />
            </Box>
          </Grid>

          {/* Right Side - VPA Inputs with Add and Remove Buttons */}
          <Grid item xs={12} md={6}>
            <Box p={6}>
              <Typography variant="h5" gutterBottom sx={{ fontWeight: "bold", color: "#1f4cb6" }}>
                Virtual Payment Address (VPA)
              </Typography>

              {/* Dynamic VPA Fields */}
              {vpas.map((vpa, index) => (
                <TextField
                  key={index}
                  fullWidth
                  label={`VPA ${index + 1}`}
                  variant="outlined"
                  value={vpa}
                  onChange={(e) => handleVpaChange(index, e.target.value)}
                  error={!!errors.vpas && !vpa}
                  helperText={errors.vpas && !vpa ? "Please enter at least one VPA." : ""}
                  sx={{ marginBottom: 3 }}
                />
              ))}

              {/* Increment and Decrement Buttons */}
              <Box display="flex" alignItems="center" justifyContent="flex-start" gap={2}>
                <IconButton
                  onClick={decrementVPA}
                  color="error"
                  sx={{ backgroundColor: "#ffcccb", ":hover": { backgroundColor: "#f99" } }}
                >
                  <Remove />
                </IconButton>
                <Typography variant="h6" sx={{ fontWeight: "500" }}>
                  VPA Count: {vpaCount}
                </Typography>
                <IconButton
                  onClick={incrementVPA}
                  color="primary"
                  sx={{ backgroundColor: "#d1eaff", ":hover": { backgroundColor: "#80c6ff" } }}
                >
                  <Add />
                </IconButton>
              </Box>

              {/* Save Button */}
              <Box mt={4}>
                <Button
                  variant="contained"
                  onClick={handleSave}
                  sx={{
                    px: 5,
                    py: 1.2,
                    borderRadius: "12px",
                    fontSize: "14px",
                    backgroundColor: "#1976d2",
                    "&:hover": {
                      backgroundColor: "#1565c0",
                    },
                    boxShadow: "none", // Minimalistic button style
                  }}
                >
                  Save
                </Button>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );
};

export default BankMappingPage;
