document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('revenueChart');
    if (!canvas) return;

    new Chart(canvas, {
        type: 'bar',
        data: {
            labels: [
                'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
                'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
            ],
            datasets: [{
                label: 'Doanh thu (triệu ₫)',
                data: [12, 19, 8, 15, 22, 30, 28, 35, 40, 38, 45, 50],
                backgroundColor: 'rgba(34, 197, 94, 0.6)',
                borderColor: '#056e2c',
                borderWidth: 1,

            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        color: '#334155',
                        font: { size: 14, weight: '600' }
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(25, 150, 71, 0.95)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    cornerRadius: 8,
                    padding: 12,
                    callbacks: {
                        label: (ctx) =>
                            `${ctx.parsed.y.toLocaleString('vi-VN')} triệu ₫`
                    }
                }
            },
            scales: {
                x: {
                    grid: { display: false },
                    ticks: {
                        color: '#64748b',
                        font: { size: 13 }
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0,0,0,0.05)',
                        drawBorder: false
                    },
                    ticks: {
                        color: '#64748b',
                        callback: (value) => value + ' triệu'
                    }
                }
            }
        }
    });
});
