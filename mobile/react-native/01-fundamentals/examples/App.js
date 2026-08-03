import React, { useState } from 'react';
import { View, Text, Button, StyleSheet, FlatList } from 'react-native';

const App = () => {
  const [count, setCount] = useState(0);
  const [items, setItems] = useState([
    { id: '1', title: 'Item 1' },
    { id: '2', title: 'Item 2' },
    { id: '3', title: 'Item 3' },
  ]);

  const addItem = () => {
    setItems([...items, { id: String(items.length + 1), title: `Item ${items.length + 1}` }]);
    setCount(count + 1);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>React Native Demo</Text>
      <Text style={styles.count}>Count: {count}</Text>
      <Button title="Add Item" onPress={addItem} />
      
      <FlatList
        data={items}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.item}>
            <Text>{item.title}</Text>
          </View>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, paddingTop: 50 },
  title: { fontSize: 24, fontWeight: 'bold', marginBottom: 20 },
  count: { fontSize: 18, marginBottom: 10 },
  item: { padding: 10, borderBottomWidth: 1, borderBottomColor: '#ccc' },
});

export default App;
