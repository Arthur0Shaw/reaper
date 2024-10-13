import React, { useEffect, useState } from "react";
import { Typography, Box, List, ListItem, ListItemText, CircularProgress } from "@mui/material";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";

interface Merchant {
  id: string; // Assuming 'id' is a string, adjust if necessary
  businessName: string;
  email: string;
}

interface MerchantListProps {
  onSelectMerchant: (merchant: Merchant) => void; // Callback function that takes a Merchant object
}

const MerchantList: React.FC<MerchantListProps> = ({ onSelectMerchant }) => {
  const [merchants, setMerchants] = useState<Merchant[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch the merchants list
  const fetchMerchants = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token"); // Assuming token is stored in localStorage
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/users/merchantList`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      setMerchants(data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching merchants:", error);
      setError("Failed to load merchants.");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMerchants(); // Fetch the merchants when the component mounts
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="200px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" flexDirection="column" height="200px">
        <ErrorOutlineIcon color="error" sx={{ fontSize: 50, mb: 2 }} />
        <Typography color="error" textAlign="center">
          {error}
        </Typography>
      </Box>
    );
  }

  if (!merchants.length) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" flexDirection="column" height="200px">
        <Typography variant="h6" color="textSecondary">
          No merchants found.
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ padding: "1.5rem", backgroundColor: "#fff", borderRadius: "12px", boxShadow: "0px 2px 6px rgba(0, 0, 0, 0.08)" }}>
      <Typography variant="h5" sx={{ fontWeight: "600", mb: 2 }}>
        Merchant List
      </Typography>
      <List>
        {merchants.map((merchant) => (
          <ListItem button key={merchant.id} onClick={() => onSelectMerchant(merchant)}>
            <ListItemText primary={merchant.businessName} secondary={merchant.email} />
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export default MerchantList;
