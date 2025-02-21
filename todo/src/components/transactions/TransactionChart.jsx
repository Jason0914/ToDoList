import React, { useMemo } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
} from "recharts";

const COLORS = [
  "#0088FE",
  "#00C49F",
  "#FFBB28",
  "#FF8042",
  "#8884d8",
  "#82ca9d",
  "#ffc658",
];

const TransactionChart = ({ transactions }) => {
  const chartData = useMemo(() => {
    if (!transactions || transactions.length === 0) {
      return [];
    }

    const categoryStats = {};
    transactions.forEach((transaction) => {
      const key = transaction.category || "其他";
      if (!categoryStats[key]) {
        categoryStats[key] = {
          category: key,
          收入: 0,
          支出: 0,
        };
      }

      const amount = Number(transaction.amount);
      if (transaction.type === "INCOME") {
        categoryStats[key].收入 += amount;
      } else {
        categoryStats[key].支出 += amount;
      }
    });

    return Object.values(categoryStats);
  }, [transactions]);

  const pieData = useMemo(() => {
    if (!transactions || transactions.length === 0) {
      return [];
    }

    const expensesByCategory = {};
    transactions
      .filter((t) => t.type === "EXPENSE")
      .forEach((t) => {
        const category = t.category || "其他";
        if (!expensesByCategory[category]) {
          expensesByCategory[category] = 0;
        }
        expensesByCategory[category] += Number(t.amount);
      });

    return Object.entries(expensesByCategory)
      .map(([name, value]) => ({
        name,
        value,
      }))
      .sort((a, b) => b.value - a.value);
  }, [transactions]);

  if (!transactions || transactions.length === 0) {
    return (
      <div className="card h-100">
        <div className="card-body text-center">
          <h5 className="card-title mb-4">收支分析</h5>
          <p>尚無交易資料</p>
        </div>
      </div>
    );
  }

  return (
    <div className="card h-100">
      <div className="card-body">
        <h5 className="card-title mb-4">收支分析</h5>
        <div style={{ width: "100%", height: "300px", position: "relative" }}>
          <ResponsiveContainer>
            <BarChart
              data={chartData}
              margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="category" />
              <YAxis />
              <Tooltip
                formatter={(value) =>
                  new Intl.NumberFormat("zh-TW", {
                    style: "currency",
                    currency: "TWD",
                    minimumFractionDigits: 0,
                  }).format(value)
                }
              />
              <Legend />
              <Bar dataKey="收入" fill="#82ca9d" />
              <Bar dataKey="支出" fill="#ff7875" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <h5 className="card-title mt-4 mb-4">支出分布</h5>
        <div style={{ width: "100%", height: "300px", position: "relative" }}>
          <ResponsiveContainer>
            <PieChart>
              <Pie
                data={pieData}
                cx="50%"
                cy="50%"
                innerRadius={0}
                outerRadius={80}
                fill="#8884d8"
                paddingAngle={5}
                dataKey="value"
                label={({ name, percent }) =>
                  `${name} (${(percent * 100).toFixed(0)}%)`
                }
              >
                {pieData.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip
                formatter={(value) =>
                  new Intl.NumberFormat("zh-TW", {
                    style: "currency",
                    currency: "TWD",
                    minimumFractionDigits: 0,
                  }).format(value)
                }
              />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default TransactionChart;
