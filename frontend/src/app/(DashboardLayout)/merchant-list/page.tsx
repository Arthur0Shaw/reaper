"use client";

import React, { useState } from "react";
import { Grid, Box, Typography, Paper } from "@mui/material";
import MerchantList from "../components/merchant/MerchantList";
import MerchantDetails from "../components/merchant/MerchantDetails";

const MerchantListPage = () => {
  const [selectedMerchant, setSelectedMerchant] = useState(null);

  const handleSelectMerchant = (merchant) => {
    setSelectedMerchant(merchant);
  };

  return (
    <Box p={3} sx={{ backgroundColor: "#f0f4f8", minHeight: "100vh" }}>
      {/* Minimalist title for Merchant Directory */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          mb: 4,
        }}
      >
        <Typography
          variant="h4"
          gutterBottom
          sx={{
            fontWeight: 600,
            color: "#333",
            padding: "10px 30px",
            borderBottom: "3px solid #1f4cb6",
            background: "linear-gradient(90deg, rgba(255,255,255,1) 0%, rgba(231,238,251,1) 100%)",
            borderRadius: "12px",
            boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)",
          }}
        >
          Merchant Directory
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Left side: List of merchants */}
        <Grid item xs={12} md={4}>
          <Paper
            elevation={3}
            sx={{
              padding: "1.5rem",
              backgroundColor: "#ffffff",
              borderRadius: "12px",
              border: "1px solid #e0e0e0",
              boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.05)",
              height: "100%",
            }}
          >
            <MerchantList onSelectMerchant={handleSelectMerchant} />
          </Paper>
        </Grid>

        {/* Right side: Merchant details */}
        <Grid item xs={12} md={8}>
          <Paper
            elevation={3}
            sx={{
              padding: "1.5rem",
              backgroundColor: "#ffffff",
              borderRadius: "12px",
              border: "1px solid #e0e0e0",
              boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.05)",
              height: "100%",
            }}
          >
            <MerchantDetails merchant={selectedMerchant} />
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default MerchantListPage;
