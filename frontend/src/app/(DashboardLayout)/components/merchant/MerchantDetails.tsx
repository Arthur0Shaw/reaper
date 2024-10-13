import React from "react";
import { Typography, Box } from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";

interface Merchant {
  id: string;
  businessName: string;
  email: string;
  mobile?: string;
  contactPersonName?: string;
  userStatus?: string;
}

const MerchantDetails: React.FC<{ merchant: Merchant | null }> = ({ merchant }) => {
  if (!merchant) {
    return (
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          height: "100%",
          textAlign: "center",
          color: "#9e9e9e",
          backgroundColor: "#fafafa",
          borderRadius: "12px",
          padding: "1.5rem",
          boxShadow: "0px 2px 6px rgba(0, 0, 0, 0.08)",
        }}
      >
        <InfoIcon sx={{ fontSize: 50, mb: 2, color: "#9e9e9e" }} />
        <Typography variant="h6" sx={{ color: "#666" }}>
          No merchant selected
        </Typography>
        <Typography variant="body2" sx={{ color: "#999" }}>
          Please select a merchant from the list
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ padding: "1.5rem", backgroundColor: "#f7f7f7", borderRadius: "12px" }}>
      <Typography variant="h5" gutterBottom sx={{ fontWeight: "600", color: "#333" }}>
        {merchant.businessName}
      </Typography>
      <Typography variant="body2" sx={{ color: "#666", mb: 1 }}>
        Email: {merchant.email}
      </Typography>
      <Typography variant="body2" sx={{ color: "#666", mb: 1 }}>
        Mobile: {merchant.mobile}
      </Typography>
      <Typography variant="body2" sx={{ color: "#666", mb: 1 }}>
        Contact Person: {merchant.contactPersonName}
      </Typography>
      <Typography variant="body2" sx={{ color: "#666" }}>
        Status: {merchant.userStatus}
      </Typography>
    </Box>
  );
};

export default MerchantDetails;
