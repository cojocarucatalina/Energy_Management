import React from 'react';

const userHomepage = () => {
  return (
    <div style={styles.container}>
      <h2>Hello, User!</h2>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: '400px',
    margin: '50px auto',
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    textAlign: 'center', 
  },
};

export default userHomepage;
