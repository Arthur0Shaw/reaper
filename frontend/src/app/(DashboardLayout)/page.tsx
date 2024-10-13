'use client'
import { Grid, Box } from '@mui/material';
import PageContainer from '@/app/(DashboardLayout)/components/container/PageContainer';
// components
import ProfitExpenses from '@/app/(DashboardLayout)/components/dashboard/ProfitExpenses';
import TrafficDistribution from '@/app/(DashboardLayout)/components/dashboard/TrafficDistribution';
import UpcomingSchedules from '@/app/(DashboardLayout)/components/dashboard/UpcomingSchedules';
import TopPayingClients from '@/app/(DashboardLayout)/components/dashboard/TopPayingClients';
import Blog from '@/app/(DashboardLayout)/components/dashboard/Blog';
import ProductSales from '@/app/(DashboardLayout)/components/dashboard/ProductSales';

const Dashboard = () => {
  // Mock data
  const dateWiseTransactionCount = {
    "2023-01-01": 10,
    "2023-01-02": 20,
  };

  const dateWiseTransactionAmount = {
    "2023-01-01": 1000,
    "2023-01-02": 2000,
  };

  const handleFilterChange = (data: any) => {
    console.log('Filter change data:', data);
  };

  return (
    <PageContainer title="Dashboard" description="this is Dashboard">
      <Box>
        <Grid container spacing={3}>
          <Grid item xs={12} lg={8}>
            <ProfitExpenses
              dateWiseTransactionCount={dateWiseTransactionCount}
              dateWiseTransactionAmount={dateWiseTransactionAmount}
              onFilterChange={handleFilterChange}
            />
          </Grid>
          <Grid item xs={12} lg={4}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <TrafficDistribution totalCount={50} />
              </Grid>
              <Grid item xs={12}>
                <ProductSales totalAmount={5000} />
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={12} lg={4}>
            <UpcomingSchedules />
          </Grid>
          <Grid item xs={12} lg={8}>
            <TopPayingClients />
          </Grid>
          <Grid item xs={12}>
            <Blog />
          </Grid>
        </Grid>
      </Box>
    </PageContainer>
  )
}

export default Dashboard;
