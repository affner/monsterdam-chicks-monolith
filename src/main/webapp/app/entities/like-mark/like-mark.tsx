import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './like-mark.reducer';

export const LikeMark = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const likeMarkList = useAppSelector(state => state.likeMark.entities);
  const loading = useAppSelector(state => state.likeMark.loading);
  const totalItems = useAppSelector(state => state.likeMark.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="like-mark-heading" data-cy="LikeMarkHeading">
        <Translate contentKey="monsterdamChicksApp.likeMark.home.title">Like Marks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.likeMark.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/like-mark/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.likeMark.home.createLabel">Create new Like Mark</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {likeMarkList && likeMarkList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('entityType')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.entityType">Entity Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entityType')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th className="hand" onClick={sort('multimediaId')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.multimediaId">Multimedia Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('multimediaId')} />
                </th>
                <th className="hand" onClick={sort('messageId')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.messageId">Message Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('messageId')} />
                </th>
                <th className="hand" onClick={sort('postId')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.postId">Post Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postId')} />
                </th>
                <th className="hand" onClick={sort('commentId')}>
                  <Translate contentKey="monsterdamChicksApp.likeMark.commentId">Comment Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('commentId')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.likeMark.liker">Liker</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {likeMarkList.map((likeMark, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/like-mark/${likeMark.id}`} color="link" size="sm">
                      {likeMark.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.LikeEntityKind.${likeMark.entityType}`} />
                  </td>
                  <td>{likeMark.createdDate ? <TextFormat type="date" value={likeMark.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {likeMark.lastModifiedDate ? (
                      <TextFormat type="date" value={likeMark.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{likeMark.createdBy}</td>
                  <td>{likeMark.lastModifiedBy}</td>
                  <td>{likeMark.deletedDate ? <TextFormat type="date" value={likeMark.deletedDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{likeMark.multimediaId}</td>
                  <td>{likeMark.messageId}</td>
                  <td>{likeMark.postId}</td>
                  <td>{likeMark.commentId}</td>
                  <td>{likeMark.liker ? <Link to={`/user-lite/${likeMark.liker.id}`}>{likeMark.liker.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/like-mark/${likeMark.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/like-mark/${likeMark.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/like-mark/${likeMark.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.likeMark.home.notFound">No Like Marks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={likeMarkList && likeMarkList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default LikeMark;
