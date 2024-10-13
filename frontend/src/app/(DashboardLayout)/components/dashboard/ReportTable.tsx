import {
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Chip,
  Paper,
} from "@mui/material";
import DashboardCard from "@/app/(DashboardLayout)/components/shared/DashboardCard";
import TableContainer from "@mui/material/TableContainer";

interface Transaction {
  id: string; // Assuming id is a string, adjust if it's a number
  uniqueId: string;
  email: string;
  status: "SUCCESS" | "FAILURE" | "PENDING"; // Restrict status to known values
  amount: number;
  createdAt: string; // Assuming createdAt is a string representing the date
}

interface ReportTableProps {
  transactions: Transaction[];
}

const ReportTable: React.FC<ReportTableProps> = ({ transactions }) => {
  return (
    <Box
      sx={{
        backgroundColor: "#fff",
        borderRadius: "16px",
        boxShadow: "0 4px 10px rgba(0, 0, 0, 0.05)",
        padding: "2rem",
        mt: 4,
      }}
    >
      {/* Title Section */}
      <Typography
        variant="h5"
        sx={{
          mb: 3,
          textAlign: "center",
          fontWeight: "bold",
          color: "#1565c0",
          letterSpacing: "0.5px",
        }}
      >
        Transaction Report
      </Typography>

      {/* Table Section */}
      <TableContainer component={Paper} sx={{ borderRadius: "12px" }}>
        <Table sx={{ whiteSpace: "nowrap" }}>
          <TableHead>
            <TableRow>
              <TableCell>
                <Typography variant="subtitle2" fontWeight={600}>
                  ID
                </Typography>
              </TableCell>
              <TableCell>
                <Typography variant="subtitle2" fontWeight={600}>
                  Unique ID
                </Typography>
              </TableCell>
              <TableCell>
                <Typography variant="subtitle2" fontWeight={600}>
                  Email
                </Typography>
              </TableCell>
              <TableCell>
                <Typography variant="subtitle2" fontWeight={600}>
                  Status
                </Typography>
              </TableCell>
              <TableCell>
                <Typography variant="subtitle2" fontWeight={600}>
                  Amount
                </Typography>
              </TableCell>
              <TableCell>
                <Typography variant="subtitle2" fontWeight={600}>
                  Date
                </Typography>
              </TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {transactions.map((transaction) => (
              <TableRow key={transaction.id} sx={{ "&:hover": { backgroundColor: "#f9f9f9" } }}>
                <TableCell>
                  <Typography
                    sx={{
                      fontSize: "15px",
                      fontWeight: "500",
                      color: "#424242",
                    }}
                  >
                    {transaction.id}
                  </Typography>
                </TableCell>
                <TableCell>{transaction.uniqueId}</TableCell>
                <TableCell>{transaction.email}</TableCell>
                <TableCell>
                  <Chip
                    size="small"
                    label={transaction.status}
                    sx={{
                      fontWeight: "500",
                      color: "#fff",
                      backgroundColor:
                        transaction.status === "SUCCESS"
                          ? "#4caf50"
                          : transaction.status === "FAILURE"
                          ? "#f44336"
                          : "#ffa726",
                    }}
                  />
                </TableCell>
                <TableCell>{transaction.amount}</TableCell>
                <TableCell>{transaction.createdAt}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default ReportTable;
