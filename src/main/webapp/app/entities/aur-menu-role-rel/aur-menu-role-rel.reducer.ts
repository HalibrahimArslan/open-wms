import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from '../../shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from '../../shared/reducers/reducer.utils';
import { IAurMenuRoleRel, defaultValue } from '../../shared/model/aur-menu-role-rel.model';

const initialState: EntityState<IAurMenuRoleRel> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/aur-menu-role-rels';

// Actions

export const getEntities = createAsyncThunk('aurMenuRoleRel/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IAurMenuRoleRel[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'aurMenuRoleRel/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IAurMenuRoleRel>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'aurMenuRoleRel/create_entity',
  async (entity: IAurMenuRoleRel, thunkAPI) => {
    const result = await axios.post<IAurMenuRoleRel>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'aurMenuRoleRel/update_entity',
  async (entity: IAurMenuRoleRel, thunkAPI) => {
    const result = await axios.put<IAurMenuRoleRel>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'aurMenuRoleRel/partial_update_entity',
  async (entity: IAurMenuRoleRel, thunkAPI) => {
    const result = await axios.patch<IAurMenuRoleRel>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'aurMenuRoleRel/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IAurMenuRoleRel>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const AurMenuRoleRelSlice = createEntitySlice({
  name: 'aurMenuRoleRel',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = AurMenuRoleRelSlice.actions;

// Reducer
export default AurMenuRoleRelSlice.reducer;
